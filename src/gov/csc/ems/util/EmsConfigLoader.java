package gov.csc.ems.util;

import gov.csc.ems.gsm.sendservice.ModemService;
import gov.csc.ems.gsm.service.impl.SmsServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class EmsConfigLoader extends HttpServlet {
	Logger loger = Logger.getLogger(EmsConfigLoader.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String ems = config.getInitParameter("ems");
		String realPathRoot = config.getServletContext().getRealPath("/");
		loger.info("开始加载系统配置文件ems.properties");
		SysUtils.initProperties(realPathRoot);
		//ModemTest.test();
//		ModemService.initialize();
		System.out.println("启动就初始化的方法111111111");
		SmsServiceImpl.telVoiceInitialize();	//初始化语音盒服务
		System.out.println("启动就初始化的方法222222222");
	}
}
