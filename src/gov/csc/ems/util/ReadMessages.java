package gov.csc.ems.util;

// ReadMessages.java - 读取并显示短信.
// www.sendsms.cn
//
// 任务:
// 1) 定义Service对象。
// 2) 设置一个或者多个短信通道Gateway。
// 3) 绑定Gateway到Service。
// 4) 设置Callback。
// 5) 运行。

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.sendsms.AGateway;
import cn.sendsms.ICallNotification;
import cn.sendsms.IGatewayStatusNotification;
import cn.sendsms.IInboundMessageNotification;
import cn.sendsms.IOrphanedMessageNotification;
import cn.sendsms.InboundMessage;
import cn.sendsms.Library;
import cn.sendsms.Service;
import cn.sendsms.AGateway.GatewayStatuses;
import cn.sendsms.AGateway.Protocols;
import cn.sendsms.InboundMessage.MessageClasses;
import cn.sendsms.Message.MessageTypes;
import cn.sendsms.modem.SerialModemGateway;

public class ReadMessages
{
	Service srv;

	public void doIt() throws Exception
	{

		// 收到的短信列表.
		List<InboundMessage> msgList;

	/*	// 处理状态报告。
		InboundNotification inboundNotification = new InboundNotification();

		// 处理语音呼入.
		CallNotification callNotification = new CallNotification();

		//处理短信通道的状态.
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		
		OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();*/

		try
		{
			System.out.println("示例: 从串口短信设备读取并显示短信.");
			System.out.println(Library.getLibraryDescription());
			System.out.println("版本: " + Library.getLibraryVersion());

			// 创建一个服务对象  

			this.srv = Service.getInstance();

			// 创建一个gateway对象，一个gateway对应一个短信设备。
			SerialModemGateway gateway = new SerialModemGateway("jindi", "COM22", 9600, "Wavecom",null);

			// 设置短信编码格式，默认为 PDU (如果只发送英文，请设置为TEXT)。
			gateway.setProtocol(Protocols.PDU);

			// 设置通道gateway是否处理接受到的短信 
			gateway.setInbound(true);

			// 设置是否可发送短信					
			gateway.setOutbound(true);

			// SIM PIN.
			gateway.setSimPin("0000");

			// 设置状态报告处理.
			
		/*	this.srv.setInboundMessageNotification(inboundNotification);
			this.srv.setCallNotification(callNotification);
			this.srv.setGatewayStatusNotification(statusNotification);
			this.srv.setOrphanedMessageNotification(orphanedMessageNotification);*/
			

			// 添加Gateway到Service对象，如果有多个Gateway，都要一一添加。
			this.srv.addGateway(gateway);

			// 启动服务 
			this.srv.startService();

			// 显示和硬件设备相关的信息.
			System.out.println();
			System.out.println("设备信息:");
			System.out.println("  厂  商: " + gateway.getManufacturer());
			System.out.println("  型  号: " + gateway.getModel());
			System.out.println("  序列号: " + gateway.getSerialNo());
			System.out.println("  IMSI号: " + gateway.getImsi());
			System.out.println("  信  号: " + gateway.getSignalLevel() + "%");
			System.out.println("  电  池: " + gateway.getBatteryLevel() + "%");
			System.out.println();
                        System.out.println("按<回车>键接收短信...");
                        System.in.read();
			//定义msgList
			msgList = new ArrayList<InboundMessage>();
			
			
			for(int j=0;j<5;j++){
				this.srv.readMessages(msgList, MessageClasses.UNREAD);
				
				if (msgList != null) {
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < msgList.size(); i++) {
						InboundMessage readBean = (InboundMessage) msgList.get(i);
						buffer.append(readBean.getMemIndex());
						buffer.append("#");
						buffer.append(readBean.getOriginator());
						buffer.append("#");
						buffer.append(readBean.getText());
						buffer.append("#");
						buffer.append(DateUtil.transferFromDateToString(readBean.getDate()));
						// buffer.append("#");
						if (i != msgList.size() - 1) {
							buffer.append("|");
						}
					}
					System.out.println(buffer.toString());
				}
				
				
				Thread.sleep(5000);
			}
			
		/*	//读取所有短信到msgList
			this.srv.readMessages(msgList, MessageClasses.UNREAD);
			
			if (msgList != null) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < msgList.size(); i++) {
					InboundMessage readBean = (InboundMessage) msgList.get(i);
					buffer.append(readBean.getMemIndex());
					buffer.append("#");
					buffer.append(readBean.getOriginator());
					buffer.append("#");
					buffer.append(readBean.getText());
					buffer.append("#");
					buffer.append(DateUtil.transferFromDateToString(readBean.getDate()));
					// buffer.append("#");
					if (i != msgList.size() - 1) {
						buffer.append("|");
					}
				}
				System.out.println(buffer.toString());
			}*/
			// 此处进入输入等待. 主要目的是为了处理收到的短信或者语音事件。
			System.out.println("按<回车>键退出...");
			System.in.read(); System.in.read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			srv.stopService();
		}
	}

	/*public class InboundNotification implements IInboundMessageNotification
	{
		public void process(String gatewayId, MessageTypes msgType, InboundMessage msg)
		{
			if (msgType == MessageTypes.INBOUND) System.out.println(">>> 收到短信: " + gatewayId);
			else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> 收到短信状态报告: " + gatewayId);
			System.out.println(msg);
			try
			{
				// 删除收到的短信.
				// ReadMessages.this.srv.deleteMessage(msg);
			}
			catch (Exception e)
			{
				System.out.println("删除短信失败...");
				e.printStackTrace();
			}
		}
	}*/

	public class InboundNotification implements IInboundMessageNotification
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg)
		{
			if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
			else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
	}
	


/*	public class CallNotification implements ICallNotification
	{
		public void process(String gatewayId, String callerId)
		{
			System.out.println(">>> 有电话呼入: " + gatewayId + " : " + callerId);
		}
	}
*/	
	public class CallNotification implements ICallNotification
	{
		public void process(AGateway gateway, String callerId)
		{
			System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

/*	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(String gatewayId, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			System.out.println(">>> 设备状态 " + gatewayId + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}
*/	
	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}
	
	
	public class OrphanedMessageNotification implements IOrphanedMessageNotification
	{
		public boolean process(AGateway gateway, InboundMessage msg)
		{
			System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
			System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned message part.
			return false;
		}
	}

	public static void main(String args[])
	{
		ReadMessages app = new ReadMessages();
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
