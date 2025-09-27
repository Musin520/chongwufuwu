package com.javaPro.myProject.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OSS直传服务
 * 提供前端直传OSS所需的签名和配置信息
 */
@Service
public class OssDirectUploadService {
    
    @Autowired(required = false)
    private OSS ossClient;
    
    @Value("${oss.endpoint}")
    private String endpoint;
    
    @Value("${oss.bucketName}")
    private String bucketName;
    
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    
    @Value("${oss.urlPrefix}")
    private String urlPrefix;
    
    /**
     * 获取OSS直传签名
     * @return 包含签名信息的Map
     */
    public Map<String, Object> getOssSignature() {
        if (ossClient == null) {
            throw new RuntimeException("OSS客户端未配置，无法获取签名");
        }

        try {
            // 设置上传回调URL（可选）
            String host = urlPrefix;
            
            // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
            String dir = "uploads/";
            
            Map<String, Object> respMap = new HashMap<>();
            
            // 用户上传文件时指定的前缀。
            respMap.put("dir", dir);
            respMap.put("host", host);
            
            // 设置该policy的失效时间，单位为毫秒
            long expireTime = 30 * 60 * 1000; // 30分钟
            long expireEndTime = System.currentTimeMillis() + expireTime;
            Date expiration = new Date(expireEndTime);
            
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            
            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("expire", expireEndTime / 1000);
            respMap.put("callback", "");
            
            return respMap;
            
        } catch (Exception e) {
            throw new RuntimeException("获取OSS签名失败", e);
        }
    }
    
    /**
     * 生成唯一的文件名
     * @param originalFilename 原始文件名
     * @return 新的文件名
     */
    public String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
