package com.javaPro.myProject.modules.product.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 宠物服务表(Product)表控制层
 *
 * @author AjaxResult
 * @since AjaxResult 07:51:54
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ProductService productService;

    /**
     * 分页查询
     *
     * @param product 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Product product) {
        startPage();
        return getList(this.productService.queryByPage(product));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.productService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param product 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Product product) {
        return AjaxResult.ok(this.productService.insert(product));
    }

    /**
     * 编辑数据
     *
     * @param product 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Product product) {
        return AjaxResult.ok(this.productService.update(product));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.productService.deleteById(id));
    }

}

