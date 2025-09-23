package com.javaPro.myProject.controller;

import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.service.ImageMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 图片迁移控制器
 * 提供图片从本地迁移到OSS的接口
 */
@RestController
@RequestMapping("/api/migration")
public class ImageMigrationController {
    
    @Autowired
    private ImageMigrationService imageMigrationService;
    
    /**
     * 迁移所有图片到OSS
     * 注意：这是一个耗时操作，建议在维护时间执行
     */
    @PostMapping("/migrateAll")
    public AjaxResult migrateAllImages() {
        try {
            imageMigrationService.migrateAllImages();
            return AjaxResult.ok("所有图片迁移完成");
        } catch (Exception e) {
            return AjaxResult.error("图片迁移失败: " + e.getMessage());
        }
    }
    
    /**
     * 只迁移产品图片
     */
    @PostMapping("/migrateProducts")
    public AjaxResult migrateProductImages() {
        try {
            imageMigrationService.migrateProductImages();
            return AjaxResult.ok("产品图片迁移完成");
        } catch (Exception e) {
            return AjaxResult.error("产品图片迁移失败: " + e.getMessage());
        }
    }
    
    /**
     * 只迁移用户头像
     */
    @PostMapping("/migrateUsers")
    public AjaxResult migrateUserImages() {
        try {
            imageMigrationService.migrateUserImages();
            return AjaxResult.ok("用户头像迁移完成");
        } catch (Exception e) {
            return AjaxResult.error("用户头像迁移失败: " + e.getMessage());
        }
    }
    
    /**
     * 只迁移公告图片
     */
    @PostMapping("/migrateNotices")
    public AjaxResult migrateNoticeImages() {
        try {
            imageMigrationService.migrateNoticeImages();
            return AjaxResult.ok("公告图片迁移完成");
        } catch (Exception e) {
            return AjaxResult.error("公告图片迁移失败: " + e.getMessage());
        }
    }
    
    /**
     * 只迁移静态图片文件
     */
    @PostMapping("/migrateStatic")
    public AjaxResult migrateStaticImages() {
        try {
            imageMigrationService.migrateStaticImages();
            return AjaxResult.ok("静态图片迁移完成");
        } catch (Exception e) {
            return AjaxResult.error("静态图片迁移失败: " + e.getMessage());
        }
    }
}
