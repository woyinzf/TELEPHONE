package gov.csc.ems.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	private static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";	
	
	public static String transferFromDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_TIME);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return sdf.format(date); 
	}
	
}
