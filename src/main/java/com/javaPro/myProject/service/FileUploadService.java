package com.javaPro.myProject.service;

import com.aliyun.oss.OSS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired(required = false)
    private OSS ossClient;

    @Value("${oss.bucketName:default-bucket}")
    private String bucketName;

    @Value("${oss.urlPrefix:http://localhost:7002/uploads/}")
    private String urlPrefix;

    @Value("${oss.accessKeyId:your-access-key-id}")
    private String accessKeyId;

    // 本地上传目录
    private final String localUploadDir = System.getProperty("user.dir") + "/uploads/";

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("文件名为空");
        }

        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }

        String fileName = UUID.randomUUID().toString() + extension;

        // 尝试使用OSS上传
        if (isOssConfigured()) {
            try {
                return uploadToOss(file, fileName);
            } catch (Exception e) {
                logger.warn("OSS上传失败，回退到本地存储: " + e.getMessage());
            }
        }

        // 回退到本地存储
        return uploadToLocal(file, fileName);
    }

    private boolean isOssConfigured() {
        return ossClient != null &&
               accessKeyId != null && !accessKeyId.equals("your-access-key-id") &&
               bucketName != null && !bucketName.equals("default-bucket");
    }

    private String uploadToOss(MultipartFile file, String fileName) throws IOException {
        String objectName = "uploads/" + fileName;
        ossClient.putObject(bucketName, objectName, file.getInputStream());
        return urlPrefix + objectName;
    }

    private String uploadToLocal(MultipartFile file, String fileName) throws IOException {
        // 确保上传目录存在
        Path uploadPath = Paths.get(localUploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件到本地
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // 返回本地访问URL
        return "/uploads/" + fileName;
    }
}