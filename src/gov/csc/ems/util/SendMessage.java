package gov.csc.ems.util;

// SendMessage.java - 金笛短信服务器 v3.4.6 发送示例.
// www.sendsms.cn


import gov.csc.ems.gsm.sendservice.impl.OutboundNotification;

import java.util.ArrayList;
import java.util.List;

import cn.sendsms.Library;
import cn.sendsms.OutboundMessage;
import cn.sendsms.Service;
import cn.sendsms.modem.SerialModemGateway;

public class SendMessage
{
	public void doIt() throws Exception
	{
		Service srv;
		OutboundMessage msg;
		//OutboundNotification outboundNotification = new OutboundNotification();
		System.out.println("示例: 通过串口短信设备发送短信.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("版本: " + Library.getLibraryVersion());

		
        //有时由于信号问题,可能会引起超时,运行时若出现No Response 请把这句注释打开
       //System.setProperty("sendsms.nocops",new String());
		System.setProperty("sendsms.serial.polling",new String());
		srv = Service.getInstance();

        //使用时请修改端口号和波特率,如果不清楚,可以去www.sendsms.com.cn下载金笛设备检测工具检测一下
		SerialModemGateway gateway = new SerialModemGateway("短信猫", "COM22", 9600, "Wavecom", null);
          

        // 设置通道gateway是否处理接受到的短信
		//gateway.setInbound(true);

        // 设置是否可发送短信
		gateway.setOutbound(true);

		gateway.setSimPin("0000");
		
		

        // 设置短信编码格式，默认为 PDU (如果只发送英文，请设置为TEXT)。CDMA设备只支持TEXT协议
      //  gateway.setProtocol(Protocols.TEXT);

        // 添加Gateway到Service对象，如果有多个Gateway，都要一一添加。
		//srv.setOutboundMessageNotification(new OutboundNotification());
		srv.addGateway(gateway);

        //启动服务
		srv.startService();
		System.out.println();
		System.out.println("设备信息:");
		System.out.println("  厂  商: " + gateway.getManufacturer());
		System.out.println("  型  号: " + gateway.getModel());
		System.out.println("  序列号: " + gateway.getSerialNo());
		System.out.println("  IMSI号: " + gateway.getImsi());
		System.out.println("  信  号: " + gateway.getSignalLevel() + "%");
		System.out.println("  电  池: " + gateway.getBatteryLevel() + "%");
		System.out.println();
		
		// 发送短信
		List<OutboundMessage> mesList = new ArrayList<OutboundMessage>();
		msg = new OutboundMessage("15201330374", "si12312 ");
		msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
		msg.setStatusReport(true);
		mesList.add(msg);
		
		srv.queueMessages(mesList);
		
		

		System.out.println("按<回车>键退出...");
		System.in.read();
		srv.stopService();
	}


	public static void main(String args[])
	{
		SendMessage app = new SendMessage();
		try
		{
			app.doIt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
