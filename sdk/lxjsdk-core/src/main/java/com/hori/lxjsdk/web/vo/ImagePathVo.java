package com.hori.lxjsdk.web.vo;

import java.io.Serializable;
/**
 * 上传的图片的访问路径信息
 * @author laizs
 * @time 2017年8月8日下午1:36:19
 *
 */
public class ImagePathVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 原图访问路径
	private String o_path = "";
	// 缩列图访问路径
	private String t_path = "";
	public String getO_path() {
		return o_path;
	}
	public void setO_path(String o_path) {
		this.o_path = o_path;
	}
	public String getT_path() {
		return t_path;
	}
	public void setT_path(String t_path) {
		this.t_path = t_path;
	}
	
}
