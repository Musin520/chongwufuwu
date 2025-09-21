package com.javaPro.myProject.modules.sysuser.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.common.util.uploadUtil;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 用户表(Sysuser)表控制层
 *
 * @author
 * @since  17:36:57
 */
@RestController
@RequestMapping("sysuser")
public class SysuserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SysuserService sysuserService;
@Resource
private com.javaPro.myProject.common.util.uploadUtil uploadUtil;
    /**
     * 分页查询
     *
     * @param sysuser 筛选条件

     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Sysuser sysuser) {
        startPage();
        return getList(this.sysuserService.queryByPage(sysuser));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.sysuserService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param sysuser 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Sysuser sysuser) {
        return AjaxResult.ok(this.sysuserService.insert(sysuser));
    }

    /**
     * 编辑数据
     *
     * @param sysuser 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Sysuser sysuser) {
        return AjaxResult.ok(this.sysuserService.update(sysuser));
    }
    @PostMapping("editUserPerson")
    public AjaxResult editUserPerson(MultipartFile file, Sysuser sysuser) {
        if (file != null){
            String upload = uploadUtil.upload(file);
            sysuser.setImg(upload);
        }
        return AjaxResult.ok(this.sysuserService.update(sysuser));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.sysuserService.deleteById(id));
    }

}

