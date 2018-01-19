package gov.csc.ems.gsm.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.csc.ems.gsm.service.ISmsService;
import gov.csc.ems.util.DateUtil;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author liuy 2013-7-15
 */
public class SmsServiceImpl implements ISmsService {
	
	public interface CLibrary extends Library {
		//加载本地拨号相关的动态文件.dll
		CLibrary INSTANCE = (CLibrary) Native.loadLibrary(
				(Platform.isWindows() ? "tw16vid" : "c"), CLibrary.class);
		
		//加载本地发音相关的动态文件.dll
		CLibrary INSTANCE__yuyin = (CLibrary) Native.loadLibrary(
				(Platform.isWindows() ? "playch32" : "c"), CLibrary.class);
		
		//定义函数，这里的函数名必须与.dll里的函数名一样，有区分大小写
		int TV_Installed(); // 判断模拟卡驱动程序是否已经安装
		int TV_Initialize(); // 初始化系统中所有的模拟电话卡，并打开语音卡终端
		void TV_OffHookCtrl(int tongdaoNum); //控制某一外线通道摘机
		int TV_ChannelType(int tongdaoNum); // 查询某一通道的类型
		void TV_StartTimer(int tongdaoNum, long times);//开始某一通道的计时器
		void TV_HangUpCtrl(int tongdaoNum); // 控制某一外线通道挂机
		int TV_StartDial(int tongdaoNum, String TelNo); // 某一通道进行自动拨号 （通道号，）
		void TV_Disable(); //禁止模拟语音卡工作
		void TV_StartMonitor(int tongdaoNum);		//开始监视被叫方的摘机状态***************************************************
		int TV_DialRest(int tongdaoNum);//查询某一通道有多少字节没有拨完
		int	TV_ListenerOffHook(int tongdaoNum); //检测极性反转电话线路中，被呼叫方是否已经摘机。 返回： 0 ： 被呼叫方未摘机; 非 0 ： 被呼叫方已摘机
		int	TV_MonitorOffHook(int tongdaoNum, int huiling_length); //监视被叫方是否摘机********************************************************
		int	TV_CheckSignal(int tongdaoNum,int[] br, int[] siglen); //查询某一通道的信号音结果 (一般来说只查询外线通道) 1. 通道号  2. 返回<信号音个数>变量指针 3. 返回<信号音长度>变量指针 (单位： 40 ms)
		void TV_CompressRatio (int yasuobi);		//设置所有通道录放音的压缩比(可以默认不用设置)
		int TV_TimerElapsed (int tongdaoNum);	//查询某通道计时器已走过的时间
		//------------------------------语音
		int PasswordDetect(String password,String path); //密码测试  （参数：密码，地址）
		int TV_StartPlayCh(int tongdaoNum,String text_wenben);//播放语音文件（参数：通道，文本）
		int TV_PlayChRest(int tongdaoNum);//查询汉字字符串是否播放完.
	}
	private static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";	
	/**
	 * 作用：向指定电话号码发送字符串文本内容
	 * @param telNum:电话号码
	 * @param telNum:播音内容
	 * 返回：“成功”。（“1”，“电话号”）
	 * 返回：“不成功，打通后，对方没有摘机”。 （“2”，“电话号”）
	 * 返回：“不成功，传入的参数不正确”。（“3”，“电话号”）
	 * 返回：“成功，未听完挂机了”。（“4”，“电话号”）
	 */
	public String sendVoice(String telNum,String telText) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(SmsServiceImpl.PATTERN_TIME);
		
		System.out.println(sdf.format(new Date())+"调用打电话的---------号码是： "+telNum+" ,内容是： "+telText);
		String returnValue = null;
		//参数不合法的判断
		if((telNum.length() != 8 && telNum.length() != 11) || telText.length() == 0 || telText.length() > 100){
			returnValue = "3,"+telNum;
			return  returnValue;
		}
		System.out.println("发语音的开始位置：--------------------------");
		CLibrary.INSTANCE.TV_OffHookCtrl(1);	//控制外线通道1摘机  
		CLibrary.INSTANCE.TV_StartTimer(1,2);	//开始某一通道的计时器
		System.out.println("摘机------------");
		while (true) {
			if((CLibrary.INSTANCE.TV_TimerElapsed(1))<0){	//摘机后延迟两秒
				break;
			}
		}
		System.out.println("开始拨号");
		CLibrary.INSTANCE.TV_StartDial(1,"0,"+telNum);	//某通道进行自动拨号  返回拨号长度  
		while ((CLibrary.INSTANCE.TV_DialRest(1))>0) {		//查询某一通道有多少字节没有拨完   
		}
		System.out.println("循环拨号后");
		CLibrary.INSTANCE.TV_StartTimer(1, 40);		//开始某一通道的计时器  开启一个计时器
		CLibrary.INSTANCE.TV_StartMonitor(1);		//开始监视被叫方的摘机状态   开通极性反转后可删除此句。
		int xinhaoyin_result = 0;
		int sigcout[]={88};
        int siglen[]=new int[1];
		while(true){
			if (CLibrary.INSTANCE.TV_ListenerOffHook(1)!=0)	//如果已摘机,跳出 （带极性反转）
				break;
			if(true){
				if (CLibrary.INSTANCE.TV_MonitorOffHook(1, 25)!=0)	// /如果已摘机,跳出 （不带极性反转）  开通极性反转，此判断删除。
					break;
			}
			//检查信号音
			xinhaoyin_result = CLibrary.INSTANCE.TV_CheckSignal(1, sigcout, siglen);
			//如果返回的是忙音，或者超过四十秒没摘机，就挂机。
			if (((xinhaoyin_result == 1 || xinhaoyin_result == 2) && sigcout[0] >= 3) || CLibrary.INSTANCE.TV_TimerElapsed (1) < 0) {
				CLibrary.INSTANCE.TV_HangUpCtrl (1);
				CLibrary.INSTANCE.TV_StartTimer(1,2);	//开始某一通道的计时器
				while (true) {
					if((CLibrary.INSTANCE.TV_TimerElapsed(1))<0){	//摘机后延迟两秒
						System.out.println("挂机后两秒延迟。处理循环调用时用的");
						break;
					}
				}
				returnValue = "2,"+telNum;
				return  returnValue;
			}
		}
		System.out.println("手机已摘机");
		int return_yuyin_num = 1001; 
		return_yuyin_num = CLibrary.INSTANCE__yuyin.TV_StartPlayCh(1, telText);
		System.out.println("开始播放语音");
		//查询文件放音中没有放完的字节数
		while(CLibrary.INSTANCE__yuyin.TV_PlayChRest(1)>0){
			xinhaoyin_result = CLibrary.INSTANCE.TV_CheckSignal(1, sigcout, siglen);
			//如果返回的是忙音 对方没有听完就挂机了，程序也要挂机。
			if ((xinhaoyin_result == 1 || xinhaoyin_result == 2) && sigcout[0] >= 3) {
				CLibrary.INSTANCE.TV_HangUpCtrl (1);
				CLibrary.INSTANCE.TV_StartTimer(1,2);	//开始某一通道的计时器
				while (true) {
					if((CLibrary.INSTANCE.TV_TimerElapsed(1))<0){	//摘机后延迟两秒
						System.out.println("挂机后两秒延迟。处理循环调用时用的");
						break;
					}
				}
				returnValue = "4,"+telNum;
				return  returnValue;
			}
		}
		CLibrary.INSTANCE.TV_HangUpCtrl(1);	//进行挂机
		System.out.println("程序挂机");
		CLibrary.INSTANCE.TV_StartTimer(1,2);	//开始某一通道的计时器
		while (true) {
			if((CLibrary.INSTANCE.TV_TimerElapsed(1))<0){	//摘机后延迟两秒
				System.out.println("挂机后两秒延迟。处理循环调用时用的");
				break;
			}
		}
		returnValue = "1,"+telNum;
		return returnValue;
	}
	
	//初始化语音盒功能
	public static void telVoiceInitialize(){
		
		System.out.println("测试加在dll文件11111111111111");
		int tongdao_allNum = CLibrary.INSTANCE.TV_Installed();
		System.out.println("测试加在dll文件aaaaaaa："+tongdao_allNum);
		if(tongdao_allNum<=0){	
			System.out.println("\n错误： TeleWind 语音卡驱动程序未安装 !\n");
		}else{
			
			//初始化  系统中所有的模拟电话卡，并打开语音卡终端 
			System.out.println("开始初始化语音卡服务的返回值:"+CLibrary.INSTANCE.TV_Initialize());	
			//设置所有通道录放音的压缩比					 
//			CLibrary.INSTANCE.TV_CompressRatio(64);
			//初始化的时候：设置语音的密码
			System.out.println("密码测试值的返回值："+CLibrary.INSTANCE__yuyin.PasswordDetect("51435815", "C:\\Windows\\System32\\"));
			
			//设置通道类型							 
			System.out.println("设置通道类型的返回值："+CLibrary.INSTANCE.TV_ChannelType(1));
		}
	}
	
}
