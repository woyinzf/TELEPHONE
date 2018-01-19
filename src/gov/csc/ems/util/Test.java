package gov.csc.ems.util;

import gov.csc.ems.gsm.service.ISmsService;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

/**
 * @author liuy 
 *  2013-7-16
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String serviceUrl = "http://localhost:8080/TELEPHONE/xfire/services/ISmsService";
		Service serviceModel = new ObjectServiceFactory().create(
				ISmsService.class, null,
				"http://localhost:8080/TELEPHONE/xfire/services/ISmsServiceISmsService?wsdl", null);
		XFireProxyFactory serviceFactory = new XFireProxyFactory();
		try {
			ISmsService service = (ISmsService) serviceFactory.create(
					serviceModel, serviceUrl);
			//String result = service.Sms_Getbatch_web_ByType(1);
			System.out.println(service.sendVoice("15010001745", "尹兆发通过语音盒发送的语音字符串，挂机"));
			//String result = service.Sms_DeleteAll_web_ByType(2);
			//System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
