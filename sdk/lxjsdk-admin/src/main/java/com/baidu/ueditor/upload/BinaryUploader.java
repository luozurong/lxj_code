package com.baidu.ueditor.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.hori.lxjsdk.utils.FileUtil;
import com.hori.lxjsdk.utils.HttpClientUtil;
import com.hori.lxjsdk.utils.StaticValue;


public class BinaryUploader {

	//public final static  String IMG_SERVER_URL="http://192.168.0.79:8090/fms/imagesUpload";
	
	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());
		
        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
			//获得文件过滤器
			File[] files = wrapper.getFiles("upfile");
			if(null!=files&&files.length==1){
				File f=files[0];
				String originFileName = wrapper.getFileNames("upfile")[0];
				String suffix = FileType.getSuffixByFilename(originFileName);
				
				File destFile = FileUtil.changeSuffix(f, suffix);
				File[] fms_files = { destFile };
				
				//String savePath = (String) conf.get("savePath");
				//savePath = savePath + suffix;
				long maxSize = ((Long) conf.get("maxSize")).longValue();

				if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
					return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
				}
				try {
					String retVal = HttpClientUtil.transImgToFms(fms_files,
							StaticValue.FMS_SERVER_URL+"/imagesUpload");
					JSONObject retObj = JSONObject.fromObject(retVal);
					String retPath=retObj.getJSONArray("list")
					.getJSONObject(0).getString("o_path");
					originFileName = originFileName.substring(0,
							originFileName.length() - suffix.length());
					retPath = PathFormat.format(retPath);
					retPath=retPath.substring(retPath.indexOf("upload")+6);
					State storageState=new BaseState(true);
					storageState.putInfo("url",retPath);
					storageState.putInfo("type", suffix);
					storageState.putInfo("original", originFileName + suffix);
					return storageState;
				} catch (Exception e) {
					// TODO: handle exception
					State storageState=new BaseState(false,AppInfo.IO_ERROR);
					e.printStackTrace();
					return storageState;
				}
				
				
				//savePath = PathFormat.parse(savePath, originFileName);

				//String physicalPath = (String) conf.get("rootPath") + savePath;
				//InputStream is = new FileInputStream(f);
				//State storageState = StorageManager.saveFileByInputStream(is,
				//		physicalPath, maxSize);
				//is.close();
				
				/*if (storageState.isSuccess()) {
					storageState.putInfo("url", PathFormat.format(savePath));
					storageState.putInfo("type", suffix);
					storageState.putInfo("original", originFileName + suffix);
				}*/

				
			}
		/*	FileItemIterator iterator = upload.getItemIterator(request);
			
			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField())
					break;
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = (String) conf.get("rootPath") + savePath;

			InputStream is = fileStream.openStream();
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			is.close();

			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;*/
		} /*catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}*/ catch (Exception e) {
			e.printStackTrace();
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
