package com.hori.grms.controller;

import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hori.grms.enums.ResultCode;
import com.hori.grms.model.Attachment;
import com.hori.grms.queryBean.CloseCaseQueryBean;
import com.hori.grms.service.CloseCaseService;
import com.hori.grms.util.UUIDGenerator;
import com.hori.grms.vo.CloseCaseVo;

/**
 * 
 * @author wangzhen
 *
 */
@RequestMapping("/closeCase")
@Controller
public class CloseCaseController extends BaseController {
	
	@Autowired
	private CloseCaseService closeCaseService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@Value("${fms_server_address}")
	private String fileUpLoadUrl;
	
	private static final String DIR = "upload/temp"; 
	
	/**
	 * ??????????????????
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET})
	public String closeCaseList() throws Exception {
		//??????????????????
	    String keyword = request.getParameter("keyword");
		String pendingParamType = request.getParameter("pendingParamType");
		if("1".equals(pendingParamType)){
			request.getSession().setAttribute("keyword", keyword);
			request.getSession().setAttribute("pendingParamType", pendingParamType);
		}
		Object roleTypeSession = request.getSession().getAttribute("roleType");
		ObjectMapper om = new ObjectMapper();
		String roleType = om.writeValueAsString(roleTypeSession);
		String userType = "";
		//????????????0?????????????????????1????????????????????????2???????????????-1
		if("0".equals(roleType)) {
			userType = "0";
		}else if("1".equals(roleType)) {
			userType = "1";
		}else if("4".equals(roleType)){	//????????????
			userType = "2";
		}else if("5".equals(roleType)) { //????????????
			userType = "2";
		}else if("6".equals(roleType)) { //????????????
			userType = "2";
		}else if("7".equals(roleType)) { //??????
			userType = "2";
		}else if("-1".equals(roleType)){ //admin
			userType = "-1";
		}
		request.getSession().setAttribute("userTypeForClose", userType);
		return "/closeCase/closeCaseList.jsp";
	}
	
	/**
	 * ????????????
	 * @param queryBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "", method = {RequestMethod.GET})
	@ResponseBody
	public Object getCloseCases(CloseCaseQueryBean queryBean) {
		Map<String, Object> result = new HashMap<>();
		try {
			Object roleTypeSession = request.getSession().getAttribute("roleType");
			Object dataAreaObject = request.getSession().getAttribute("dataArea");
			Object userAccountObject = request.getSession().getAttribute("userAccount");
			List<String> departList = (List<String>) request.getSession().getAttribute("departList");
			ObjectMapper om = new ObjectMapper();
			String roleType = om.writeValueAsString(roleTypeSession);
			String dataArea = om.writeValueAsString(dataAreaObject);
			String userAccount = (String) userAccountObject;
			Integer roleTypeInt = Integer.valueOf(roleType);
			//????????? 0:?????? 1:??????  2:??????
			if("0".equals(dataArea)) {
				//??????????????????????????????????????????????????????
				if(roleTypeInt == 0) {
					queryBean.setProjectCreaterAccount(userAccount);
					queryBean.setRoleType(roleTypeInt);
				//????????????????????????
				}else if(roleTypeInt == 1){
					queryBean.setRoleType(0);
				}else {
					queryBean.setCreaterAccount(userAccount);
					queryBean.setRoleType(roleTypeInt);
				}
			}else if("1".equals(dataArea)) {
				//????????????????????????????????????????????????????????????????????????
				if("4".equals(roleType)) {
					int departSize = 0;
					if(departList != null) {
						departSize = departList.size();
					}
					//?????????????????????id?????????????????????????????????
					if(departSize > 1) {
						queryBean.setCreaterAccount(null);
					}else {
						queryBean.setCreaterAccount(userAccount);
					}
				}else {
					queryBean.setCreaterAccount(null);
				}
				//???????????????
				if(roleTypeInt == 1) {
					queryBean.setRoleType(0);
				}else {
					queryBean.setRoleType(roleTypeInt);
				}
			}else if("2".equals(dataArea)) {
				queryBean.setCreaterAccount(null);
				queryBean.setRoleType(null);
				queryBean.setProjectCreaterAccount(null);
			}
			
			//??????????????????
			String pendingParamType = (String)request.getSession().getAttribute("pendingParamType");
			if("1".equals(pendingParamType) && pendingParamType != null){
				String keyword = (String)request.getSession().getAttribute("keyword");
				queryBean.setPageNumber(1);
				queryBean.setPageSize(10);
				queryBean.setKeyword(keyword);
			}
			
			int total = closeCaseService.getCloseCaseCount(queryBean);
			List<CloseCaseVo> infos = closeCaseService.getCloseCases(queryBean);
			result.put("code", ResultCode.SUCCESS.getCode());
			result.put("total", total);
			result.put("data", infos);
		} catch (Exception e) {
			result.put("code", ResultCode.ERROR.getCode());
			e.printStackTrace();
		} finally {
			//??????????????????
			request.getSession().removeAttribute("pendingParamType");
			request.getSession().removeAttribute("paramType");
		}
		return result;
	}
	
	/**
	 * ??????????????????/????????????
	 * @param ids
	 * @param commitStatus
	 * @return
	 */
	@RequestMapping(value = "/commit", method = {RequestMethod.POST})
	@ResponseBody
	public Object updateStatusForAction(String ids, Integer commitStatus) {
		Map<String, Object> result = new HashMap<>();
		try {
			if(StringUtils.isNotBlank(ids) && commitStatus != null) {
				Object roleTypeSession = request.getSession().getAttribute("roleType");
				ObjectMapper om = new ObjectMapper();
				String roleType = om.writeValueAsString(roleTypeSession);
				int status = 0;
				//???????????????????????????????????????
				if(commitStatus == 0) {
					if("4".equals(roleType)) {
						status = 1;
					}else if("5".equals(roleType)) {
						status = 3;
					}else if("6".equals(roleType)) {
						status = 2;
					}else if("7".equals(roleType)) {
						status = 4;
					}
				}else {
					status = 5;
				}
				if(status != 0) {
					closeCaseService.updateStatusForAction(Arrays.asList(ids.split(",")), status, Integer.valueOf(roleType));
					result.put("code", ResultCode.SUCCESS.getCode());
				}
			}
		} catch (Exception e) {
			result.put("code", ResultCode.ERROR.getCode());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ??????????????????/????????????
	 * @param closeCaseCodes
	 * @param handleStatus
	 * @return
	 */
	@RequestMapping(value = "/handle", method = {RequestMethod.POST})
	@ResponseBody
	public Object updateStatusForBusiness(String ids, Integer handleStatus) {
		Map<String, Object> result = new HashMap<>();
		try {
			if(StringUtils.isNotBlank(ids) && handleStatus != null) {
				int status = 0;
				if(handleStatus == 0) {
					status = 6;
				}else if(handleStatus == 1) {
					status = 7;
				}
				if(status != 0) {
					closeCaseService.updateStatusForBusiness(Arrays.asList(ids.split(",")), status);
					result.put("code", ResultCode.SUCCESS.getCode());
				}
			}
		} catch (Exception e) {
			result.put("code", ResultCode.ERROR.getCode());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ????????????
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/upload", method = {RequestMethod.POST})
	@ResponseBody
	public Object uploadAttachment(@RequestParam("file") MultipartFile file, 
			@RequestParam("attachments") String attachmentString) {
		Map<String, Object> result = new HashMap<>();
		File tempFile = null;
		try {
			if(file != null) {
				String url = null;
				String fileName = file.getOriginalFilename();
				String realPath = request.getSession().getServletContext().getRealPath(DIR);
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
				String randomString = String.format("%04d", RandomUtils.nextInt());
				File dir = new File(realPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				String newFileName = dateString + randomString + "." + suffix;
				String filePath = realPath + File.separator + newFileName;
				//????????????
				tempFile = new File(filePath);
				file.transferTo(tempFile);
				//?????????fms?????????
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				FileSystemResource fsr = new FileSystemResource(tempFile);
				MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
				params.add("file", fsr);
				String fmsUrl = fileUpLoadUrl + "/filesUpload";
				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
				String resultJson = restTemplate.postForObject(fmsUrl, requestEntity, String.class);
				ObjectMapper om = new ObjectMapper();
				Map<String,Object> resultMap = om.readValue(resultJson, Map.class);
				if(resultMap != null && !resultMap.isEmpty()) {
					String res = (String) resultMap.get("result");
					if(StringUtils.isNotBlank(res) && "0".equals(res)) {
						List<Map<String,String>> paths = (List<Map<String,String>>) resultMap.get("list");
						if(paths != null && !paths.isEmpty()) {
							url = paths.get(0).get("path");
						}
					}
				}
				if(StringUtils.isNotBlank(url)) {
					List<Map<String,Object>> attachmentMap = null;
					//???????????????????????????
					attachmentString = HtmlUtils.htmlUnescape(attachmentString);
					if(StringUtils.isNotBlank(attachmentString)) {
						attachmentMap = om.readValue(attachmentString, List.class);
					}
					List<Attachment> attachments = new ArrayList<>();
					if(attachmentMap != null && !attachmentMap.isEmpty()) {
						Attachment attach = null;
						for(Map<String,Object> attachMap : attachmentMap) {
							String correlationId = (String) attachMap.get("correlationId");
							if(StringUtils.isNotBlank(correlationId)) {
								attach = new Attachment();
								attach.setId(UUIDGenerator.generate());
								attach.setFileUrl(url);
								attach.setFileName(fileName);
								attach.setType(4);
								attach.setCorrelationId(correlationId);
								attach.setIsExtra((Integer) attachMap.get("isExtra"));
								attachments.add(attach);
							}
						}
					}
					closeCaseService.uploadAttachment(attachments);
					result.put("code", ResultCode.SUCCESS.getCode());
				}
			}else {
				result.put("code", 1);
			}
		} catch (Exception e) {
			result.put("code", ResultCode.ERROR.getCode());
			e.printStackTrace();
		} finally {
			//??????????????????
			if(tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		return result;
	}
	
	/**
	 * ????????????
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
	public void downloadAttachment(String id) throws Exception {
		try {
			if(StringUtils.isNotBlank(id)) {
				Attachment attachment = closeCaseService.getAttachmentById(id);
				if(attachment != null) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
					ResponseEntity<byte[]> res = restTemplate.exchange(attachment.getFileUrl(), HttpMethod.GET, 
							httpEntity, byte[].class);
					if(res.getStatusCode() == HttpStatus.OK) {
						response.reset();
						response.setContentType("APPLICATION/OCTET-STREAM");
						response.setCharacterEncoding("utf-8");
						response.setHeader("Content-Disposition", "attachment;filename=" + 
								URLEncoder.encode(attachment.getFileName(), "UTF-8"));
						byte[] result = res.getBody();
						if(result == null) {
							result = new byte[0];
						}
						response.getOutputStream().write(result);
					}
				}
			}
		} catch (Exception e) {
			response.reset();
			response.getWriter().println("server error");
			e.printStackTrace();
		}
	}
	
	/**
	 * ????????????
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteAttachment", method = {RequestMethod.POST})
	@ResponseBody
	public Object deleteAttachment(String ids) {
		Map<String, Object> result = new HashMap<>();
		try {
			if(StringUtils.isNotBlank(ids)) {
				closeCaseService.deleteAttachments(Arrays.asList(ids.split(",")));
				result.put("code", ResultCode.SUCCESS.getCode());
			}else {
				result.put("code", 1);
			}
		} catch (Exception e) {
			result.put("code", ResultCode.ERROR.getCode());
			e.printStackTrace();
		}
		return result;
	}
	
}
