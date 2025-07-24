package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.file.UploadFileRspVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aoao
 * @create 2025-07-22-14:08
 */
public interface FileService {
    UploadFileRspVO uploadFile(MultipartFile file);
}
