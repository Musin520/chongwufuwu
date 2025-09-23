package com.javaPro.myProject.controller;

import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.service.OssDirectUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * OSS直传控制器
 * 提供前端直传OSS所需的签名接口
 */
@RestController
@RequestMapping("/api/oss")
public class OssDirectUploadController {
    
    @Autowired
    private OssDirectUploadService ossDirectUploadService;
    
    /**
     * 获取OSS直传签名
     * @return 签名信息
     */
    @GetMapping("/signature")
    public AjaxResult getOssSignature() {
        try {
            Map<String, Object> signature = ossDirectUploadService.getOssSignature();
            return AjaxResult.ok(signature);
        } catch (Exception e) {
            return AjaxResult.error("获取OSS签名失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成唯一文件名
     * @param originalFilename 原始文件名
     * @return 新文件名
     */
    @GetMapping("/generateFileName")
    public AjaxResult generateFileName(@RequestParam String originalFilename) {
        try {
            String fileName = ossDirectUploadService.generateFileName(originalFilename);
            return AjaxResult.ok(fileName);
        } catch (Exception e) {
            return AjaxResult.error("生成文件名失败: " + e.getMessage());
        }
    }
}
