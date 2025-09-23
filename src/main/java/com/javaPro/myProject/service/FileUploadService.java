package com.javaPro.myProject.service;

import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {
    
    @Autowired
    private OSS ossClient;
    
    @Value("${oss.bucketName}")
    private String bucketName;
    
    @Value("${oss.urlPrefix}")
    private String urlPrefix;
    
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        String objectName = "uploads/" + fileName;
        
        ossClient.putObject(bucketName, objectName, file.getInputStream());
        
        return urlPrefix + objectName;
    }
}