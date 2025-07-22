package com.aoao.blog.admin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author aoao
 * @create 2025-07-22-13:24
 */
@ConfigurationProperties(prefix = "minio")
@Data
@Component
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
