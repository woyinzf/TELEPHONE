package gov.csc.ems.util;

public class IConstants {
	/**
	 * 数据库配置项webservice地址
	 */
	public static final String GSM_WEBSERVICE_ENDPOINT = "gsm.webservice.endpoint";

	/**
	 * 短信猫设备网关
	 */
	public static final String GSM_MODEM_GATEWAY="gsm.modem.gateway";
	public static final String MODEM_GATEWAY="modem.gateway";
	public static final String SUCCESS="1";
	public static final String FAILED="0";
	
	
	/*
	 * Sms_Type：短信类型(0：未读短信；1：已读短信；2：全部短信)
	 */
	public static final int SMS_TYPE_UNREAD = 0;
	public static final int SMS_TYPE_ALREAD = 1;
	public static final int SMS_TYPE_ALL = 2;
	
}
