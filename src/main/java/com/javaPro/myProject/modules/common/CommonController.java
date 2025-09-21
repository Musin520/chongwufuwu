package com.javaPro.myProject.modules.common;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.type.entity.TType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件控制层
 */
@RestController
@RequestMapping("common")
public class CommonController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private com.javaPro.myProject.common.util.uploadUtil uploadUtil;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @PostMapping("upload")
    public AjaxResult queryByPage(MultipartFile file) {
        String upload = uploadUtil.upload(file);
       return AjaxResult.ok(upload);
    }


}

