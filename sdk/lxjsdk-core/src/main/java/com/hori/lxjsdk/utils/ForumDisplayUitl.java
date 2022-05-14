package com.hori.lxjsdk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 论坛各种显示规则工具类
 * @author FWQ
 *
 */
public class ForumDisplayUitl {

	/**
	 * 帖子发布时间显示规则
	 * @return
	 */
	public static String topicSubjectTimeDisplay(Date createTime){
		long minute = 1000*60;
		long hour = minute*60;
		long day = hour*24;
		
		long time = createTime.getTime();
		long now = System.currentTimeMillis();
		long num =now - time;
		
		if (num<hour) {//1小时内
			if(num/minute==0){
				return "1分钟前";
			}else {
				return num/minute+"分钟前";
			}
		}else if (num>=hour && num<day) {//1小时以上，1天内
			return num/hour+"小时前";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.format(createTime);
		}
	}
	
	/**
	 * 帖子阅读数显示规则
	 * @return
	 */
	public static String topicSubjectReadNumDisplay(int currentNum){
		int divideNum = currentNum / 1000;
		int mod = currentNum % 1000;
		if(divideNum == 0){
			return currentNum+"";
		}else{
			for(int i=0;i<10;i++){
				if(mod > i*100 && mod < (i+1)*100){
					if(i==0){
						return divideNum+"K";
					}else{
						return divideNum+"."+i+"K";
					}
					
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 取总页数
	 * @param totalCount	总记录数
	 * @param pageSize	每页的记录数
	 * @return
	 */
	public static long getTotalPageCount(long totalCount , int pageSize) {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String create = "2017-08-21 10:25:26";
//		Date date = DateUtil.changeStringToDate(create);
//		System.out.println(ForumDisplayUitl.topicSubjectTimeDisplay(date));
		
		System.out.println(ForumDisplayUitl.topicSubjectReadNumDisplay(3609));
	}
}
