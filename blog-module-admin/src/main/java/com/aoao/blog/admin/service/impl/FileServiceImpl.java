package com.aoao.blog.admin.service.impl;

import com.aoao.blog.common.model.admin.vo.file.UploadFileRspVO;
import com.aoao.blog.admin.service.FileService;
import com.aoao.blog.admin.util.MinioUtil;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aoao
 * @create 2025-07-22-14:08
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public UploadFileRspVO uploadFile(MultipartFile file) {
        try {
            String url = minioUtil.upload(file);
            return new UploadFileRspVO(url);
        } catch (Exception e) {
            throw new BizException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
    }
}
