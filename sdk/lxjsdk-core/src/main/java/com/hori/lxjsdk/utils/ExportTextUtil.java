package com.hori.lxjsdk.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导出文件文件的工具类
 */
public class ExportTextUtil {
        /**
         * 声明日志记录器
         */
        private static final Logger logger = LoggerFactory.getLogger(ExportTextUtil.class);

        /**
         * 页大小
         */
        private static final int PAGE_SIZE = 1000;
        /**
         * 限制最长长度
         */
        private static final int SENSITIVEWORDMAXLIMIT = 70;
        /**
         * 限制最短长度
         */
        private static final int SENSITIVEWORDMINLIMIT = 2;
        /**
         * 导出文本文件  因为这个比较特殊所以批量处理 想用直接复制一份代码
         * @param response
         * @param sensitiveWordOperationService
         * @param fileName
         */
       /* public static void writeToTxt(HttpServletResponse response,SensitiveWordOperationService sensitiveWordOperationService,long wordCount,String fileName) {//设置响应的字符集
            response.setCharacterEncoding("utf-8");
            //设置响应内容的类型
            response.setContentType("text/plain");
            //设置文件的名称和格式
            response.addHeader(
                    "Content-Disposition",
                    "attachment; filename="
                            + FileUtil.genAttachmentFileName(fileName, "过滤敏感词.txt"));//通过后缀可以下载不同的文件格式
           // BufferedOutputStream buff = null;
            ServletOutputStream outStr = null;
            OutputStreamWriter osw=null;
	        BufferedWriter bw=null;
            try {
                outStr = response.getOutputStream();
                //buff = new BufferedOutputStream(outStr);
                osw = new OutputStreamWriter(outStr);
	            bw =new BufferedWriter(osw);
                
                // 根据行数求数据提取次数
                int  list_count  = new Long(wordCount).intValue();
                int export_times = list_count % PAGE_SIZE > 0 ? list_count / PAGE_SIZE  
                        + 1 : list_count / PAGE_SIZE;  
                AtomicInteger integer = new AtomicInteger(0);
                for (int i = 0; i < export_times; i++) { 
                	 List<String> list =  sensitiveWordOperationService.getSensitiveWord(i*PAGE_SIZE, PAGE_SIZE);
                	 for (int j = 0; j < list.size(); j++) {
                		 //bw.append(list.get(j)).append("\n");
                		 bw.append(list.get(j));
                		 integer.incrementAndGet();
                		 if(integer.intValue()<list_count)
                			 bw.append("\r\n");
					}
                	list.clear();
                }
                //bw.flush();
            } catch (Exception e) {
            	logger.error("导出文件文件出错，e:{}",e);
            } finally {
            	try {
            		if(bw!=null){
    	                try {
    	                    bw.close();
    	                    bw=null;
    	                } catch (IOException e) {
    	                    e.printStackTrace();
    	                } 
    	            }
    	            if(osw!=null){
    	                try {
    	                    osw.close();
    	                    osw=null;
    	                } catch (IOException e) {
    	                    e.printStackTrace();
    	                } 
    	            }
    	            if(outStr!=null){
    	                try {
    	                	outStr.close();
    	                	outStr=null;
    	                } catch (IOException e) {
    	                    e.printStackTrace();
    	                } 
    	            }
                } catch (Exception e) {
                	logger.error("关闭流对象出错 e:{}",e);
                }
            }
        }*/
        
        /**
	     * 导入
	     * 
	     * @param file Txt文件(路径+文件)
	     * @return
	     */
	    public static List<String> importToTxt(File file){
	        List<String> dataList=new ArrayList<String>();
	        BufferedReader br=null;
	        try { 
	            br = new BufferedReader(new FileReader(file));
	            String line = ""; 
	            while ((line = br.readLine()) != null) { 
	            	if(StringUtils.isBlank(line.trim()))//不需要为空
	            		continue;
	            	if(line.trim().length()>SENSITIVEWORDMAXLIMIT||line.trim().length()<SENSITIVEWORDMINLIMIT)//长度大于70的放弃
	            		continue;
	                dataList.add(line.trim());
	            }
	        }catch (Exception e) {
	        	logger.error("导入文件数据出错 e:{}",e);
	        }finally{
	            if(br!=null){
	                try {
	                    br.close();
	                    br=null;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return dataList;
	    }

        /**
         * 如果字符串对象为 null，则返回空字符串，否则返回去掉字符串前后空格的字符串
         * @param str
         * @return
         */
        public static String delNull(String str) {
                String returnStr="";
                if (StringUtils.isNotBlank(str)) {
                    returnStr=str.trim();
                }
                return returnStr;
        }
}
