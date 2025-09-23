package com.javaPro.myProject.controller;

import com.javaPro.myProject.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @PostMapping("/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            String url = fileUploadService.uploadFile(file);
            result.put("success", true);
            result.put("url", url);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}