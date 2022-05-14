/**
 * 
 */
package com.hori.grms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hori.grms.dao.AttachmentMapper;
import com.hori.grms.model.Attachment;
import com.hori.grms.service.AttachmentService;
@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService {
	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	public List<Attachment> findBycorrelationId(String correlationId,Integer type) {		
		return attachmentMapper.findBycorrelationId(correlationId,type);
	}

	@Override
	public List<Attachment> findBycorrelationIdForClose(String projectCode, int i) {
		return  attachmentMapper.findBycorrelationIdForClose(projectCode, i);
	}
}
