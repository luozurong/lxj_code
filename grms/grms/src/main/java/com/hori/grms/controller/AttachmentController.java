/**
 * 
 */
package com.hori.grms.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.attachment.AttachmentSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.hori.grms.component.FmsUploadUtil;
import com.hori.grms.enums.ResultCode;
import com.hori.grms.model.Attachment;
import com.hori.grms.model.CloseCaseInfo;
import com.hori.grms.pageModel.Json;
import com.hori.grms.util.PictureUtil;
import com.hori.grms.util.Result;
import com.hori.grms.vo.FilePathVo;

/**
 * @ClassName: AttachmentController
 * @Description: 附件表
 * @author zhuqiang
 * @date 2018年8月13日 上午10:00:57
 */
@Controller
@RequestMapping("/attachment")
public class AttachmentController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//图片文件存放临时文件夹
	private static final String DIRETORY="upload/temp";
	//读取培正文件路径
	private String storePath;
	@Resource(name="fmsUploadUtil")
	private FmsUploadUtil fmsUploadUtil;
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/uploadProjectFile", method = RequestMethod.POST,produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String uploadProjectFile(HttpServletRequest request, @RequestParam MultipartFile file) {
		Json json = new Json();
		
		//保存的临时文件
		File tempFile=null;		
		try {
			//文件名
			String fileName = file.getOriginalFilename();
			
			
			//先把文件存到临时文件夹，文件http上传至fms后，再删除临时文件
			//文件存放的路径
			String dirPath=null;
			dirPath= request.getSession().getServletContext().getRealPath(DIRETORY);
			
            String contextPath = request.getSession().getServletContext().getContextPath();
			
			if(null==dirPath){
				//使用项目的相对路径找到绝对路径
				String location = request.getSession().getServletContext().getRealPath(contextPath);
				dirPath = location+"/"+DIRETORY;
			}
			logger.info("文件上传存放目录："+dirPath);
			File directory=new File(dirPath);
			if(!directory.exists()){
				directory.mkdirs();
			}
			//文件后缀
			String fileSuffix=file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
			String tempFileName=PictureUtil.generateFileName();
			//新文件名
			String newFileName=tempFileName+"."+fileSuffix;
			//原图文件路径
			String filepath=dirPath+File.separator+newFileName;
			logger.info("临时文件路径："+filepath);
			tempFile=new File(filepath);
			file.transferTo(tempFile); //保存文件
			//文件上传fms系统
			Map<String, Object> resResult=new HashMap<String, Object>(); 
			resResult=this.fmsUploadUtil.transfilesToFmsNoRename(new File[]{tempFile});
	
			if (((String)resResult.get("result")).equals("false")){
				throw new RuntimeException(fileName+":文件上传失败");
			}
			List<FilePathVo> filePathVos=(List<FilePathVo>) resResult.get("list");
			
			
			String fileUrl=filePathVos.get(0).getPath();
			
			if(StringUtils.isBlank(fileUrl)){
				throw new RuntimeException(fileName+":文件上传失败");
			}
			
			Attachment attachment=new Attachment();
			attachment.setId(UUID.randomUUID().toString().replace("-", ""));
			attachment.setFileName(fileName);
			attachment.setFileUrl(fileUrl);
			attachment.setType(1);
			
			HttpSession session=request.getSession();
			List<Attachment> attchments=null;
			
			String attchmentsStr=(String) session.getAttribute("attchmentsFromStr");
			
			if(StringUtils.isBlank(attchmentsStr)){
				attchments=new ArrayList<>();
			}else{
				attchments=JSON.parseArray(attchmentsStr, Attachment.class);
			}
			
			if(null==attchments||attchments.size()==0)  attchments=new ArrayList<>();
			
			attchments.add(attachment);
			
			session.setAttribute("attchmentsFromStr", JSON.toJSONString(attchments));
			
			json.setObj(attachment);			
			json.setSuccess(true);
			json.setMsg("文件上传成功");
			logger.info("上传成功！！！");
		} catch (Exception e) {
			e.printStackTrace();

			json.setMsg("文件上传错误");
		} finally {
			//删除临时文件
			if( null!=tempFile && tempFile.exists()){
				tempFile.delete();
			}
			
			return JSON.toJSONString(json);
		}
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}
	
	/**
	 * 文件session逻辑删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteAttchment",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String deleteAttchment(HttpServletRequest request,@RequestParam("id") String id ){
		Json json = new Json();
		try{
			HttpSession session=request.getSession();
			
			
			String attchmentsStr=(String) session.getAttribute("attchmentsFromStr");
			
			List<Attachment> attchments=null;
			if(StringUtils.isBlank(attchmentsStr)){
				throw new RuntimeException("文件不存在");
			}else{
				attchments=JSON.parseArray(attchmentsStr, Attachment.class);
			}
						
			boolean flag=false;
			for(int i=0;i<attchments.size();i++){
				if(attchments.get(i).getId().equals(id.trim())){
					Attachment rmAttachment=attchments.remove(i);										
					json.setSuccess(true);
					json.setMsg("文件删除成功");	
					
					session.setAttribute("attchmentsFromStr", JSON.toJSONString(attchments));
					
					flag=true;
					
					//将要删除的文件信息保存==>本意用于删除文件服务器的文件？未操作
					//List<Attachment> attchmentRMs=(List<Attachment>) session.getAttribute("attchmentsFromRM");
					String attchmentsRMStr=(String) session.getAttribute("attchmentsFromRMStr");
					
					List<Attachment> attchmentsRMS=null;
					if(StringUtils.isBlank(attchmentsRMStr)){
						attchmentsRMS=new ArrayList<>();
					}else{
						attchmentsRMS=JSON.parseArray(attchmentsRMStr, Attachment.class);
					}
										
					attchmentsRMS.add(rmAttachment);
					session.setAttribute("attchmentsFromRMStr", JSON.toJSONString(attchmentsRMS));	
					break;
				}
			}	
			if(!flag) new RuntimeException("未找到文件");
		}catch(Exception e){
			logger.info(e.getMessage());
			json.setMsg("文件删除错误");
		}finally{
			return JSON.toJSONString(json);
		}		
		
	}
	
	/**
	 * 下载附件
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
	public void downloadAttachment(String id) throws Exception {
		 HttpServletResponse response=getResponse();	
         HttpSession session=getSession();
	
		try {
			if(StringUtils.isNotBlank(id)) {
                String attchmentsFromStr=(String) session.getAttribute("attchmentsFromStr");
                List<Attachment> list=JSON.parseArray(attchmentsFromStr, Attachment.class);
                Attachment attachment=null;
                for (Attachment att : list) {
					if(id.equals(att.getId())){
						attachment=att;
						break;
					}
				}
				if(attachment!=null) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
					
					ResponseEntity<byte[]> res = restTemplate.exchange(attachment.getFileUrl(), HttpMethod.GET, 
							httpEntity, byte[].class);
					if(res.getStatusCode() == HttpStatus.OK) {
						String fileName = attachment.getFileName();
						// 设置下载文件类型
						response.setContentType("application/octet-stream;charset=ISO-8859-1");
						// 设置下载文件头
						String agent = getRequest().getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie
							fileName = java.net.URLEncoder.encode(fileName, "UTF8");
							response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
						} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等
							fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
							response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
						}
						response.getOutputStream().write(res.getBody());
					}
				}
			}
		} catch (Exception e) {
			response.getWriter().println("server error");
			e.printStackTrace();
		}
	}
	/**
	 * 根据URL下载附件
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadUrl", method = {RequestMethod.GET, RequestMethod.POST})
	public void downloadAttachmentUrl(String url,String fileName) throws Exception {
		 HttpServletResponse response=getResponse();	
         HttpSession session=getSession();
	
		try {
			if(StringUtils.isNotBlank(url)) {
          
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
					
					ResponseEntity<byte[]> res = restTemplate.exchange(url, HttpMethod.GET, 
							httpEntity, byte[].class);
					if(res.getStatusCode() == HttpStatus.OK) {
						// 设置下载文件类型
						response.setContentType("application/octet-stream;charset=ISO-8859-1");
						// 设置下载文件头
						String agent = getRequest().getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie
							fileName = java.net.URLEncoder.encode(fileName, "UTF8");
							response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
						} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等
							fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
							response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
						}
						response.getOutputStream().write(res.getBody());
					}
				}
			
		} catch (Exception e) {
			response.getWriter().println("server error");
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/uploadContractFile", method = RequestMethod.POST,produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String uploadContractFile(HttpServletRequest request, @RequestParam MultipartFile file) {
		Json json = new Json();
		
		//保存的临时文件
		File tempFile=null;		
		try {
			//文件名
			String fileName = file.getOriginalFilename();
			
			
			//先把文件存到临时文件夹，文件http上传至fms后，再删除临时文件
			//文件存放的路径
			String dirPath=null;
			dirPath= request.getSession().getServletContext().getRealPath(DIRETORY);
			
            String contextPath = request.getSession().getServletContext().getContextPath();
			
			if(null==dirPath){
				//使用项目的相对路径找到绝对路径
				String location = request.getSession().getServletContext().getRealPath(contextPath);
				dirPath = location+"/"+DIRETORY;
			}
			logger.info("文件上传存放目录："+dirPath);
			File directory=new File(dirPath);
			if(!directory.exists()){
				directory.mkdirs();
			}
			//文件后缀
			String fileSuffix=file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
			String tempFileName=PictureUtil.generateFileName();
			//新文件名
			String newFileName=tempFileName+"."+fileSuffix;
			//原图文件路径
			String filepath=dirPath+File.separator+newFileName;
			logger.info("临时文件路径："+filepath);
			tempFile=new File(filepath);
			file.transferTo(tempFile); //保存文件
			//文件上传fms系统
			Map<String, Object> resResult=new HashMap<String, Object>(); 
			resResult=this.fmsUploadUtil.transfilesToFmsNoRename(new File[]{tempFile});
	
			if (((String)resResult.get("result")).equals("false")){
				throw new RuntimeException(fileName+":文件上传失败");
			}
			List<FilePathVo> filePathVos=(List<FilePathVo>) resResult.get("list");
			
			
			String fileUrl=filePathVos.get(0).getPath();
			
			if(StringUtils.isBlank(fileUrl)){
				throw new RuntimeException(fileName+":文件上传失败");
			}
			
			Attachment attachment=new Attachment();
			attachment.setId(UUID.randomUUID().toString().replace("-", ""));
			attachment.setFileName(fileName);
			attachment.setFileUrl(fileUrl);
			attachment.setType(2);
			attachment.setIsExtra(0);
			HttpSession session=request.getSession();
			List<Attachment> attchments=null;
			
			String attchmentsStr=(String) session.getAttribute("attchmentsFromContract");
			
			if(StringUtils.isBlank(attchmentsStr)){
				attchments=new ArrayList<>();
			}else{
				attchments=JSON.parseArray(attchmentsStr, Attachment.class);
			}
			
			if(null==attchments||attchments.size()==0)  attchments=new ArrayList<>();
			
			attchments.add(attachment);
			
			session.setAttribute("attchmentsFromContract", JSON.toJSONString(attchments));
			
			json.setObj(attachment);			
			json.setSuccess(true);
			json.setMsg("文件上传成功");
			logger.info("上传成功！！！");
		} catch (Exception e) {
			e.printStackTrace();

			json.setMsg("文件上传错误");
		} finally {
			//删除临时文件
			if( null!=tempFile && tempFile.exists()){
				tempFile.delete();
			}
			
			return JSON.toJSONString(json);
		}
	}
	/**
	 * 文件session逻辑删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteAttchmentContract",produces="text/html;charset=UTF-8;")
	@ResponseBody
	public String deleteAttchmentContract(HttpServletRequest request,@RequestParam("id") String id ){
		Json json = new Json();
		try{
			HttpSession session=request.getSession();
			
			
			String attchmentsStr=(String) session.getAttribute("attchmentsFromContract");
			
			List<Attachment> attchments=null;
			if(StringUtils.isBlank(attchmentsStr)){
				throw new RuntimeException("文件不存在");
			}else{
				attchments=JSON.parseArray(attchmentsStr, Attachment.class);
			}
						
			boolean flag=false;
			for(int i=0;i<attchments.size();i++){
				if(attchments.get(i).getId().equals(id.trim())){
					Attachment rmAttachment=attchments.remove(i);										
					json.setSuccess(true);
					json.setMsg("文件删除成功");	
					
					session.setAttribute("attchmentsFromContract", JSON.toJSONString(attchments));
					
					flag=true;
					
					//将要删除的文件信息保存==>本意用于删除文件服务器的文件？未操作
					//List<Attachment> attchmentRMs=(List<Attachment>) session.getAttribute("attchmentsFromRM");
				/*	String attchmentsRMStr=(String) session.getAttribute("attchmentsFromRMStr");
					
					List<Attachment> attchmentsRMS=null;
					if(StringUtils.isBlank(attchmentsRMStr)){
						attchmentsRMS=new ArrayList<>();
					}else{
						attchmentsRMS=JSON.parseArray(attchmentsRMStr, Attachment.class);
					}
										
					attchmentsRMS.add(rmAttachment);
					session.setAttribute("attchmentsFromRMStr", JSON.toJSONString(attchmentsRMS));	*/
					break;
				}
			}	
			if(!flag) new RuntimeException("未找到文件");
		}catch(Exception e){
			logger.info(e.getMessage());
			json.setMsg("文件删除错误");
		}finally{
			return JSON.toJSONString(json);
		}		
		
	}
	
}
