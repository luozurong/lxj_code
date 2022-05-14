package com.hori.lxjsdk.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author sucs
 *
 */
public class CollectionUtil {
	
	/**
	 * 按分页方式来获取list的子集合
	 * @param list
	 * @param pagecode
	 * @param size
	 * @return
	 */
	public static <T> List<T> subList(List<T> list,int pagecode,int size){

		if(isEmpty(list)){
			return list;
		}
		
		int totalpage=(int) Math.ceil(list.size()*1.0/size);
		pagecode=pagecode>totalpage?totalpage:pagecode;
		
		int startoffest=(pagecode-1)*size;
		
		int endoffest=(startoffest+size)>list.size()?list.size():(startoffest+size);
		
		return list.subList(startoffest, endoffest);
		
	}
	
	/**
	 * 获取list的子集合
	 * @param num 子集合的元素个数
	 * @return
	 */
	public static <T> List<T> subList(List<T> list,int num){

		if(isEmpty(list)){
			return list;
		}
		
		int startoffest=0;
		
		int endoffest=num>list.size()?list.size():num;
		
		return list.subList(startoffest, endoffest);
		
	}
	
	
	/**
	 * 判断某个元素在数组中是否存在
	 * @param data
	 * @param arrays
	 * @return
	 */
	public static boolean inArray(Object data,Object... arrays){
		
		for (Object object : arrays) {
			if(data.equals(object))
				return true;
		}
		return false;
	}
	
	/**
	 * 判断某个元素在数组中是否存在
	 * @param data
	 * @param arrays
	 * @return
	 */
	public static boolean inArray(Integer data,Integer... arrays){
		
		for (Object object : arrays) {
			if(data.equals(object))
				return true;
		}
		return false;
	}
	
	/**
	 * 判断某个元素在数组中是否存在
	 * @param data
	 * @param arrays
	 * @return
	 */
	public static boolean inArray(String data,String... arrays){
		
		for (String str : arrays) {
			if(data.equals(str))
				return true;
		}
		return false;
	}
	
	
	/**
	 * new一个ArrayList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> newList(Object... datas){
		
		ArrayList<Object> list=new ArrayList<Object>();
		
		for (int i = 0; datas!=null && i < datas.length; i++) {
			list.add(datas[i]);
		}
		
		return (ArrayList<T>) list;
	}
	
	/**
	 * new一个ArrayList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> newList(String... datas){
		
		ArrayList<String> list=new ArrayList<String>();
		
		for (int i = 0; datas!=null && i < datas.length; i++) {
			list.add(datas[i]);
		}
		
		return (ArrayList<T>) list;
	}
	/**
	 * new一个HashMap
	 * @return
	 */
	public static <K,V> HashMap<K,V> newMap(){
		return new HashMap<K,V>();
	}
	
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }
	public static void sort(List<Integer> specs) {
		Collections.sort(specs);
	}  
	
	
	public static void main(String[] args) {
		
	}

}
