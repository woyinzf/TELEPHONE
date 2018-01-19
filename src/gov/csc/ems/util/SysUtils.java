package gov.csc.ems.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class SysUtils {
	/**
	 * 系统配置文件的相对路径
	 */
	private static final String SYS_PROP_FILE = "/WEB-INF/prop/ems.properties";

	/**
	 * 存放配置文件中键值对应关系的对象
	 */
	private static Properties properties;

	/**
	 * initProperties ：初始化配置参数
	 */
	public static void initProperties(String realPathRoot) {
		if (properties == null) {
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(realPathRoot
						+ SYS_PROP_FILE));
			} catch (Exception t) {
				System.out.println("ERROR！！系统配置文件ems.properties读取失败！");
				t.printStackTrace();
			}

			if (is != null) {
				try {
					properties = new Properties();
					properties.load(is);
					is.close();
					System.out.println("系统配置文件ems.properties读取成功！");
				} catch (Exception t) {
					System.out.println("ERROR！！系统配置文件ems.properties读取失败！");
					t.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * getProperty ：根据键获取值
	 * 
	 * @param key：键
	 * @return String：值
	 */
	public static String getProperty(String key) {
		if(properties == null){
			return "";
		}
		return properties.getProperty(key);
	}

	/**
	 * getProperty ：根据键获取值，如果没有找到配置项，返回一个默认值
	 * 
	 * @param key：键
	 * @param default_value：默认值
	 * @return String：值
	 */
	public static String getProperty(String key, String default_value) {
		String rtn = getProperty(key);
		if (rtn == null) {
			rtn = default_value;
		}
		return rtn;
	}

	/**
	 * getIntProperty ：根据键获取值，并转化为int类型，如果没有找到配置项，返回一个默认值
	 * 
	 * @param key：键
	 * @param default_value：默认值
	 * @return int：值
	 */
	public static int getIntProperty(String key, int default_value) {
		String rtn = getProperty(key);
		if (rtn == null) {
			return default_value;
		}
		return Integer.parseInt(rtn);
	}

	/**
	 * getIntProperty ：根据键获取值，并转化为float类型，如果没有找到配置项，返回一个默认值
	 * 
	 * @param key：键
	 * @param default_value：默认值
	 * @return float：值
	 */
	public static float getFloatProperty(String key, float default_value) {
		String rtn = getProperty(key);
		if (rtn == null) {
			return default_value;
		}
		return Float.parseFloat(rtn);
	}

	/**
	 * getIntProperty ：根据键获取值，并转化为double类型，如果没有找到配置项，返回一个默认值
	 * 
	 * @param key：键
	 * @param default_value：默认值
	 * @return double：值
	 */
	public static double getFloatProperty(String key, double default_value) {
		String rtn = getProperty(key);
		if (rtn == null) {
			return default_value;
		}
		return Double.parseDouble(rtn);
	}

}
