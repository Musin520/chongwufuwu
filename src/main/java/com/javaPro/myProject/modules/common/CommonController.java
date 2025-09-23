package com.javaPro.myProject.modules.common;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制层
 */
@RestController
@RequestMapping("common")
public class CommonController extends BaseController {
    /**
     * OSS文件上传服务
     */
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传到OSS
     *
     * @param file 上传的文件
     * @return 上传结果，包含OSS文件URL
     */
    @PostMapping("upload")
    public AjaxResult upload(MultipartFile file) {
        try {
            String ossUrl = fileUploadService.uploadFile(file);
            return AjaxResult.ok(ossUrl);
        } catch (Exception e) {
            return AjaxResult.error("文件上传失败: " + e.getMessage());
        }
    }


}

