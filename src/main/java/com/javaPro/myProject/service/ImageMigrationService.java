package com.javaPro.myProject.service;

import com.aliyun.oss.OSS;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.sysuser.dao.SysuserDao;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.webnotice.dao.WebnoticeDao;
import com.javaPro.myProject.modules.webnotice.entity.Webnotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 图片迁移服务
 * 将本地存储的图片迁移到OSS
 */
@Service
public class ImageMigrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageMigrationService.class);
    
    @Autowired
    private OSS ossClient;
    
    @Value("${oss.bucketName}")
    private String bucketName;
    
    @Value("${oss.urlPrefix}")
    private String urlPrefix;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private SysuserDao sysuserDao;
    
    @Resource
    private WebnoticeDao webnoticeDao;
    
    /**
     * 迁移所有图片到OSS
     */
    public void migrateAllImages() {
        logger.info("开始迁移图片到OSS...");
        
        try {
            // 迁移产品图片
            migrateProductImages();
            
            // 迁移用户头像
            migrateUserImages();
            
            // 迁移公告图片
            migrateNoticeImages();
            
            // 迁移静态图片文件
            migrateStaticImages();
            
            logger.info("图片迁移完成！");
            
        } catch (Exception e) {
            logger.error("图片迁移失败", e);
            throw new RuntimeException("图片迁移失败", e);
        }
    }
    
    /**
     * 迁移产品图片
     */
    public void migrateProductImages() {
        logger.info("开始迁移产品图片...");
        
        List<Product> products = productDao.queryAllByLimit(new Product());
        
        for (Product product : products) {
            boolean updated = false;
            
            // 迁移主图片
            if (product.getImg() != null && isLocalPath(product.getImg())) {
                String ossUrl = uploadLocalImageToOss(product.getImg());
                if (ossUrl != null) {
                    product.setImg(ossUrl);
                    updated = true;
                    logger.info("产品 {} 主图片迁移成功: {}", product.getId(), ossUrl);
                }
            }
            
            // 迁移详情图片
            if (product.getDetailimg() != null && !product.getDetailimg().isEmpty()) {
                String newDetailImg = migrateDetailImages(product.getDetailimg());
                if (!newDetailImg.equals(product.getDetailimg())) {
                    product.setDetailimg(newDetailImg);
                    updated = true;
                    logger.info("产品 {} 详情图片迁移成功", product.getId());
                }
            }
            
            // 更新数据库
            if (updated) {
                productDao.update(product);
            }
        }
        
        logger.info("产品图片迁移完成");
    }
    
    /**
     * 迁移用户头像
     */
    public void migrateUserImages() {
        logger.info("开始迁移用户头像...");
        
        List<Sysuser> users = sysuserDao.queryAllByLimit(new Sysuser());
        
        for (Sysuser user : users) {
            if (user.getImg() != null && isLocalPath(user.getImg())) {
                String ossUrl = uploadLocalImageToOss(user.getImg());
                if (ossUrl != null) {
                    user.setImg(ossUrl);
                    sysuserDao.update(user);
                    logger.info("用户 {} 头像迁移成功: {}", user.getId(), ossUrl);
                }
            }
        }
        
        logger.info("用户头像迁移完成");
    }
    
    /**
     * 迁移公告图片
     */
    public void migrateNoticeImages() {
        logger.info("开始迁移公告图片...");
        
        List<Webnotice> notices = webnoticeDao.queryAllByLimit(new Webnotice());
        
        for (Webnotice notice : notices) {
            if (notice.getImg() != null && isLocalPath(notice.getImg())) {
                String ossUrl = uploadLocalImageToOss(notice.getImg());
                if (ossUrl != null) {
                    notice.setImg(ossUrl);
                    webnoticeDao.update(notice);
                    logger.info("公告 {} 图片迁移成功: {}", notice.getId(), ossUrl);
                }
            }
        }
        
        logger.info("公告图片迁移完成");
    }
    
    /**
     * 迁移静态图片文件
     */
    public void migrateStaticImages() {
        logger.info("开始迁移静态图片文件...");
        
        String staticImgPath = System.getProperty("user.dir") + "/src/main/resources/static/img/";
        File imgDir = new File(staticImgPath);
        
        if (imgDir.exists() && imgDir.isDirectory()) {
            File[] files = imgDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".jpeg") || 
                name.toLowerCase().endsWith(".png") || 
                name.toLowerCase().endsWith(".gif"));
            
            if (files != null) {
                for (File file : files) {
                    try {
                        String ossUrl = uploadFileToOss(file);
                        logger.info("静态图片 {} 迁移成功: {}", file.getName(), ossUrl);
                    } catch (Exception e) {
                        logger.error("静态图片 {} 迁移失败", file.getName(), e);
                    }
                }
            }
        }
        
        logger.info("静态图片文件迁移完成");
    }
    
    /**
     * 判断是否为本地路径
     */
    private boolean isLocalPath(String path) {
        return path != null && (path.startsWith("\\product\\") || 
                               path.startsWith("\\static\\img\\") || 
                               path.startsWith("/product/") || 
                               path.startsWith("/static/img/"));
    }
    
    /**
     * 上传本地图片到OSS
     */
    private String uploadLocalImageToOss(String localPath) {
        try {
            // 构建本地文件路径
            String fileName = extractFileName(localPath);
            String fullPath = System.getProperty("user.dir") + "/src/main/resources/static/img/" + fileName;
            
            File file = new File(fullPath);
            if (!file.exists()) {
                logger.warn("本地文件不存在: {}", fullPath);
                return null;
            }
            
            return uploadFileToOss(file);
            
        } catch (Exception e) {
            logger.error("上传本地图片到OSS失败: {}", localPath, e);
            return null;
        }
    }
    
    /**
     * 上传文件到OSS
     */
    private String uploadFileToOss(File file) throws IOException {
        String extension = getFileExtension(file.getName());
        String fileName = UUID.randomUUID().toString() + extension;
        String objectName = "migrated/" + fileName;
        
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ossClient.putObject(bucketName, objectName, inputStream);
            return urlPrefix + objectName;
        }
    }
    
    /**
     * 迁移详情图片JSON数组
     */
    private String migrateDetailImages(String detailImgJson) {
        // 这里需要解析JSON数组，迁移每个图片URL
        // 简化处理，实际应该使用JSON解析
        return detailImgJson; // 暂时返回原值
    }
    
    /**
     * 从路径中提取文件名
     */
    private String extractFileName(String path) {
        if (path.contains("\\")) {
            return path.substring(path.lastIndexOf("\\") + 1);
        } else if (path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
