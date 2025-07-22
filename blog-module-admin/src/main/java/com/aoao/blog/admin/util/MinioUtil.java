package com.aoao.blog.admin.util;

import com.aoao.blog.admin.config.MinioConfig;
import com.aoao.blog.admin.properties.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author aoao
 * @create 2025-07-22-13:29
 */
@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    /**
     * 上传文件
     */
    public String upload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file.isEmpty()) {
            return null;
        }
        // 获取文件名称
        final String originalFilename = file.getOriginalFilename();
        // 文件的 Content-Type
        String contentType = file.getContentType();
        // 获取后缀
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成存储对象的名称（将 UUID 字符串中的 - 替换成空字符串）
        String key = UUID.randomUUID().toString().replace("-", "");
        // 拼接文件名
        String objectName = String.format("%s%s", key, ext);
        // 上传文件
        // 上传文件至 Minio
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build());
        // 返回文件的访问链接
        String url = String.format("%s/%s/%s", minioProperties.getEndpoint(), minioProperties.getBucketName(), objectName);
        return url;
    }


}
