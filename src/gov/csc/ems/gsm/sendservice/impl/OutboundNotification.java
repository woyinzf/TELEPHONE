package gov.csc.ems.gsm.sendservice.impl;
/**
 * 短信猫设备提供服务
 * @author liuy
 * 2013-7-15 
 */

import java.util.concurrent.atomic.AtomicInteger;
import cn.sendsms.AGateway;
import cn.sendsms.IOutboundMessageNotification;
import cn.sendsms.OutboundMessage;
import cn.sendsms.OutboundMessage.MessageStatuses;

public class OutboundNotification implements IOutboundMessageNotification
	{
		//成功短信的手机号
		private  String successNum = "";
		
		//失败短信的手机号
		private  String failNum = "";
		
		//用于统计发送短信的个数
		private AtomicInteger telCount = new AtomicInteger();
	


		public String getSuccessNum() {
			return successNum;
		}


		public void setSuccessNum(String successNum) {
			this.successNum = successNum;
		}




		public String getFailNum() {
			return failNum;
		}


		public void setFailNum(String failNum) {
			this.failNum = failNum;
		}


		public void process(AGateway gateway, OutboundMessage msg)
		{
			//短信发送状态
			MessageStatuses ms =  msg.getMessageStatus();
			
			switch(ms)
			{
			case SENT:
				//已发送
				synchronized("success"){
					successNum += msg.getRecipient()+",";
				}
				break;
			case UNSENT:
			case FAILED:
				//未发送及发送失败
				synchronized("fail"){
					failNum += msg.getRecipient()+",";
				}
				break;
			default:
				break;
			
			}
			
			telCount.incrementAndGet();
		}


		public AtomicInteger getTelCount() {
			return telCount;
		}


		public void setTelCount(AtomicInteger telCount) {
			this.telCount = telCount;
		}


		
	}
	
	
	
	
	
	