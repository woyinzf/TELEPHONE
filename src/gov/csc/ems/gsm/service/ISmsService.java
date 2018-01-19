package gov.csc.ems.gsm.service;

import java.util.List;

/**
 * 
 * @author Qiaohz 2011-7-9 下午07:41:16
 */
public interface ISmsService {
	
	/**
	 * 作用：向指定电话号码发送字符串文本内容
	 * @param telNum:电话号码
	 * @param telNum:播音内容
	 * 返回：“成功”。（“1”，“电话号”）
	 * 返回：“不成功，打通后，对方没有摘机”。 （“2”，“电话号”）
	 * 返回：“不成功，传入的参数不正确”。（“3”，“电话号”）
	 * 返回：“成功，未听完挂机了”。（“4”，“电话号”）
	 */
	public String sendVoice(String telNum,String telText);
	
}
