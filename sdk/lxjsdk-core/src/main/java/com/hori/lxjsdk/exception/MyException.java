package com.hori.lxjsdk.exception;
/**
 * 自定义异常
 * @author laizs
 * @file MyException.java
 * @time 2017下午5:47:21
 */
public class MyException extends Exception {
	private static final long serialVersionUID = 1L;
	public MyException(){
		super();
	}
	public MyException(String message){
		super(message);
	}
}
