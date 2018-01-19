package gov.csc.ems.gsm.sendservice;

import gov.csc.ems.gsm.sendservice.impl.OutboundNotification;
import gov.csc.ems.util.IConstants;
import gov.csc.ems.util.SysUtils;

import java.io.IOException;

import org.apache.log4j.Logger;

import cn.sendsms.GatewayException;
import cn.sendsms.Library;
import cn.sendsms.SendSMSException;
import cn.sendsms.Service;
import cn.sendsms.TimeoutException;
import cn.sendsms.AGateway.Protocols;
import cn.sendsms.modem.SerialModemGateway;

public class ModemService {
	private static Logger log = Logger.getLogger(ModemService.class);

	private static Service srv;

	public static void initialize() {
		System.out.println("示例: 通过串口短信设备发送短信.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("版本: " + Library.getLibraryVersion());

		// 有时由于信号问题,可能会引起超时,运行时若出现No Response 请把这句注释打开
		// System.setProperty("sendsms.nocops",new String());
		System.setProperty("sendsms.serial.polling", new String());
		srv = Service.getInstance();
		String coms = SysUtils.getProperty(IConstants.MODEM_GATEWAY);
		int port = Integer.valueOf(SysUtils.getProperty(IConstants.GSM_MODEM_GATEWAY).split(",")[1]);
		String[] comArray = coms.split(",");

		try {
			for (String comPort : comArray) {
				// 使用时请修改端口号和波特率,如果不清楚,可以去www.sendsms.com.cn下载金笛设备检测工具检测一下
				SerialModemGateway gateway = new SerialModemGateway("jindi",
						comPort, port, "Wavecom", null);
				
				// 设置短信编码格式，默认为 PDU (如果只发送英文，请设置为TEXT)。
				gateway.setProtocol(Protocols.PDU);

				// 设置通道gateway是否处理接受到的短信
				gateway.setInbound(true);
				
				// 设置是否可发送短信
				gateway.setOutbound(true);
				gateway.setSimPin("0000");
				// 添加Gateway到Service对象，如果有多个Gateway，都要一一添加。
				srv.addGateway(gateway);
			}

			// 启动服务
			srv.startService();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (SendSMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void restartService() {
		stopService();
		initialize();
	}

	public static void stopService() {
		if (srv != null) {
			try {
				log.info("开始停止短信猫服务...");
				srv.stopService();
				log.info("已成功停止短信猫服务!!");
			} catch (TimeoutException e) {
				log.error("连接超时无法正常停止短信猫服务!!");
				e.printStackTrace();
			} catch (GatewayException e) {
				log.error("网关异常无法正常停止短信猫服务!!");
				e.printStackTrace();
			} catch (IOException e) {
				log.error("IO异常无法正常停止短信猫服务!!");
				e.printStackTrace();
			} catch (InterruptedException e) {
				log.error("内部错误无法正常停止短信猫服务!!");
				e.printStackTrace();
			} catch (SendSMSException e) {
				log.error("短信发送失败!!");
				e.printStackTrace();
			}
		}
	}

	public static Service getService() {
		if (srv == null) {
			initialize();
		}
		return srv;
	}

	/*
	 * public static Service getService(String comName) { if (srvMap == null) {
	 * synchronized (ModemService.class) { if(srvMap == null) initialize(); } }
	 * return srvMap.get(comName); }
	 */
}
