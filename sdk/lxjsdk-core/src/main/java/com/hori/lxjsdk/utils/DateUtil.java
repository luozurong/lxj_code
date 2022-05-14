package com.hori.lxjsdk.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.jlit.utils.StringUtil;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 *  to correct time pattern. Minutes should be mm not MM (MM is month). 
 */
public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);
	private static final String TIME_PATTERN = "HH:mm";

	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private DateUtil() {
	}
	
	
	/**
	 * 获取当天的时间字符串， eg:2013-01-29 23:59:59 用于一次性获取 个人诊断信息等
	 * 
	 * @return
	 */
	public static String getDataFormat() {
		StringBuffer sb = new StringBuffer();
		Calendar c = new GregorianCalendar();
		sb.append(c.get(c.YEAR));
		sb.append("-");
		sb.append(spliStringLength(c.get(c.MONTH) + 1));
		sb.append("-");
		sb.append(spliStringLength(c.get(c.DATE)));
		sb.append(" 23:59:59");
		return sb.toString();
	}

	/**
	 * 获取当天2个月前的时间字符串， eg:2013-01-29 23:59:59 用于一次性获取 个人诊断信息等
	 * 
	 * @return
	 */
	public static String getDataFormatTowMonthBefore(int monthCount) {
		StringBuffer sb = new StringBuffer();
		Calendar c = new GregorianCalendar();
		sb.append(c.get(c.YEAR));
		sb.append("-");
		sb.append(spliStringLength(c.get(c.MONTH) + 1 - monthCount));
		sb.append("-");
		sb.append(spliStringLength(c.get(c.DATE)));
		sb.append(" 00:00:00");
		return sb.toString();
	}

	public static String spliStringLength(int data) {
		String value = data + "";
		if (value.length() == 2) {
			return value;
		} else {
			return "0" + value;
		}

	}

	public static Date changeStringToDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date uDate = null;
		try {   
			uDate = sdf.parse(strDate);  
		} catch (ParseException e) {   
		    e.printStackTrace();   
		}  
		return uDate;
	}
	
	public static String changeDateToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(date);
		return strDate;
	}

	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		String defaultDatePattern;
		try {
			//  defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
			//    .getString("date.format");
			defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
		}

		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss";
	}

	/**
	 * This method attempts to convert an Oracle-formatted date
	 * in the form dd-MMM-yyyy to mm/dd/yyyy.
	 *
	 * @param aDate date from database as a string
	 * @return formatted string for the ui
	 */
	public static String getDate(Date aDate) {
		SimpleDateFormat df;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time
	 * in the format you specify on input
	 *
	 * @param aMask the date pattern the string is in
	 * @param strDate a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			//log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format:
	 * MM/dd/yyyy HH:MM a
	 *
	 * @param theTime the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(TIME_PATTERN, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time
	 * in the format you specify on input
	 *
	 * @param aMask the date pattern the string is in
	 * @param aDate a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date based
	 * on the System Property 'dateFormat'
	 * in the format you specify on input
	 * 
	 * @param aDate A date to convert
	 * @return a string representation of the date
	 */
	public static String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	public static String convertDateToNormalString(Date aDate) {
		return getDateTime("yyMMdd", aDate);
	}

	public static String convertDateToMonthString(Date aDate) {
		return getDateTime("yyMM", aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date aDate = null;

		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}

			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + strDate + "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return aDate;
	}

	/**
	 * 判断是否是同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isTheSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}
	/**
	 * #时间间隔，每次从mysql表中获取最后一条记录时间 减去该时间间隔(S)
	 */
	public static Date changeDateInterval(Date d,int interval){
		@SuppressWarnings("deprecation")
		Date newDate = new Date(d.getYear(),d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getSeconds()-interval);
		return newDate;
	}
	
	/**
	 * 将时间转换成时分秒
	 * FM:2014-01-23 18:27:52
	 * TO:17时35分05秒
	 * @return
	 */
	public static String changeDateToSFM(String dateString){
		String back = "";
		StringBuffer sb = new StringBuffer();
		try{
			String time = dateString.split(" ")[1];
			String [] times = time.split(":");
			sb.append(times[0]);
			sb.append("时");
			sb.append(times[1]);
			sb.append("分");
			sb.append(times[2]);
			sb.append("秒");
			back = sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return back;
	}
	
	/**
	 * 拼装string为指定的字符串
	 * @param dateValue
	 * @return
	 */
	public static String splicStringToDate(String dateValue){
		StringBuffer sb = new StringBuffer();
		String [] values = dateValue.split("\\ ");
		sb.append(values[0]);
		sb.append(" 23:59:59");
		return sb.toString();
	}
	
	//获取当前的年份
	public static String getCurrentYear(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy"); 
		String yearDay = "";
		//获取前月的最后一天
		 Calendar cale = Calendar.getInstance();   
	     yearDay = cale.get(Calendar.YEAR)+"";
        
		return yearDay;
	}
	
	//获取当前的月份
	public static String getCurrentMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy"); 
		String monthDay = "";
		//获取前月的最后一天
		 Calendar cale = Calendar.getInstance();   
		 monthDay = (cale.get(Calendar.MONTH)+1)+"";
		 if("0".equals(monthDay)){
			 return "12";
		 }
        
		return monthDay;
	}
	
	/**
	 * 通过年，月获取天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysByYearAndMonth(String years, String months) {  
		int year = Integer.parseInt(years);
		int month = Integer.parseInt(months);
	    int days = 0;  
	    if (month != 2) {  
	        switch (month) {  
	        case 1:  
	        case 3:  
	        case 5:  
	        case 7:  
	        case 8:  
	        case 10:  
	        case 12:  
	        days = 31;  
	        break;  
	        case 4:  
	        case 6:  
	        case 9:  
	        case 11:  
	        days = 30;  
	  
	        }  
	    } else {  
	        //闰年  
	        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)  
	        days = 29;  
	        else  
	        days = 28;  
	  
	    }  
	    return days;
	}
	
	/**
	 * 获取昨 天的年月日
	 * @param date
	 * @return
	 */
	public static String getBeforeDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return DateUtil.changeDateToString(date);
	}
	
	/**
	 * 获取明 天的年月日
	 * @param date
	 * @return
	 */
	public static String getTomorrowDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return DateUtil.changeDateToString(date);
	}
	
	/**
	 * 拼装string为指定的开始时间
	 * @param dateValue
	 * @return
	 */
	public static String splicStringToBeginTime(String dateValue){
		StringBuffer sb = new StringBuffer();
		String [] values = dateValue.split("\\ ");
		sb.append(values[0]);
		sb.append(" 00:00:00");
		return sb.toString();
	}
	
	
	/**
	 * 拼装string为指定的结束时间
	 * @param dateValue
	 * @return
	 */
	public static String splicStringToEndTime(String dateValue){
		StringBuffer sb = new StringBuffer();
		String [] values = dateValue.split("\\ ");
		sb.append(values[0]);
		sb.append(" 23:59:59");
		return sb.toString();
	}
	
	//获取前月的第一天
	public static String getFirstDayForPreviousMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String firstDay = "";
		//获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH,-1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        firstDay = format.format(cal_1.getTime());
        
		return firstDay+" 00:00:00";
	}
	
	//获取前月的最后一天
	public static String getLastDayForPreviousMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String lastDay = "";
		//获取前月的最后一天
		 Calendar cale = Calendar.getInstance();   
	     cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
	     lastDay = format.format(cale.getTime());
        
		return lastDay+" 23:59:59";
	}
	
	//获取当月的第一天
	public static String getFirstDayForCurrentMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String firstDay = "";
		//获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH,0);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        firstDay = format.format(cal_1.getTime());
        
		return firstDay+" 00:00:00";
	}
	
	//获取当月的最后一天
	public static String getLastDayForCurrentMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 Calendar ca = Calendar.getInstance();    
	     ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
	     String last = format.format(ca.getTime());
        
		return last+" 23:59:59";
	}

	public static void main(String[] args) {
		DateUtil du = new DateUtil();
		String s = "2013-01-29 23:59:59";
		try {
//			System.out.println("DateUtil:" + DateUtil.convertStringToDate(s));
//			Date date = DateUtil.convertStringToDate(s);
//			System.out.println("DateUtil:" + DateUtil.changeDateToString(new Date()));
			
			
			
//			Date date = DateUtil.changeStringToDate(s);
//			int interval = Integer.parseInt("10");
//			Date newDate = DateUtil.changeDateInterval(date, interval);
//			String newTime = DateUtil.changeDateToString(newDate);
//			
//			System.out.println("HzjbxxTask  s:" + s +" newTime:"+newTime);
			
			
//			System.out.println(""+du.getCurrentYear() +"  :  "+ du.getCurrentMonth());
			
//			String beforeDate = DateUtil.getBeforeDay(DateUtil.changeStringToDate("2016-01-14 00:00:45"));
//			String beforeYear = beforeDate.substring(0,4);
//			String beforeMonth = beforeDate.substring(5,7);
//			String beforeDay = beforeDate.substring(8,10);
//			System.out.println(beforeYear+" : "+beforeMonth+" : "+beforeDay+"a");
//			
//			String splicTime = beforeYear+"-"+beforeMonth+"-"+beforeDay;
//			
//			String beginTime = DateUtil.splicStringToBeginTime(splicTime);
//			String endTime = DateUtil.splicStringToEndTime(splicTime);
//			System.out.println(beginTime+" || "+endTime);
//			Date date = DateUtil.changeStringToDate("2016-03-15 00:00:00");
//			String beforeDate = DateUtil.changeDateToString(date);
//			//获取当前时间上个月的起始，结束时间
//			String beforeYear = beforeDate.substring(0,4);	
//			String beforeMonth = StringUtil.delFirstZero(beforeDate.substring(5,7));
//			if(beforeMonth != null && "1".equals(beforeMonth)){
//				beforeYear = (Integer.parseInt(beforeYear) - 1)+"";
//				beforeMonth = "12";
//			}else{
//				beforeMonth = (Integer.parseInt(beforeMonth) - 1)+""; 
//			}
//			
//			int daySize = DateUtil.getDaysByYearAndMonth(beforeYear, beforeMonth);
//			String beginTime =  beforeYear+"-"+StringUtil.equipAllTheLength(beforeMonth, 2)+"-01 00:00:00";
//			String endTime = beforeYear+"-"+StringUtil.equipAllTheLength(beforeMonth, 2)+"-"+daySize+" 00:00:00";
//			
//			System.out.println(beginTime+" || "+endTime);
//			
//			//循环
//			List<String> moList = new ArrayList<String>();
//			moList.add("2014");
//			moList.add("2015");
//			for(String mo:moList){
//				for(int i=1;i<=12;i++){
//					String dates = mo+"-"+StringUtil.equipAllTheLength(i+"", 2)+"-01 00:00:00";
//					System.out.println(" dates: "+dates);
//				}
//			}
			
			//循环
//			List<String> moList = new ArrayList<String>();
//			moList.add("2014");
//			moList.add("2015");
//			for(String mo:moList){
//				for(int i=1;i<=12;i++){
//					int daySize = DateUtil.getDaysByYearAndMonth(mo, i+"");
//					for(int j=1;j<=daySize;j++){
//						String dates = mo+"-"+StringUtil.equipAllTheLength(i+"", 2)+"-"+StringUtil.equipAllTheLength(j+"", 2)+" 00:00:00";
//						System.out.println(" dates: "+dates);
//					}
//				}
//			}
			
			System.out.println("hello:"+DateUtil.getTomorrowDay(new Date()));
			System.out.println("first:"+DateUtil.getFirstDayForPreviousMonth());
			System.out.println("last:"+DateUtil.getLastDayForPreviousMonth());
			System.out.println("first:"+DateUtil.getFirstDayForCurrentMonth());
			System.out.println("last:"+DateUtil.getLastDayForCurrentMonth());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
