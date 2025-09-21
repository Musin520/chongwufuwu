package com.javaPro.myProject.modules.orderEvalute.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.orderEvalute.entity.OrderEvalute;
import com.javaPro.myProject.modules.orderEvalute.service.OrderEvaluteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评价表(OrderEvalute)表控制层
 */
@RestController
@RequestMapping("orderEvalute")
public class OrderEvaluteController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private OrderEvaluteService orderEvaluteService;

    /**
     * 分页查询
     *
     * @param orderEvalute 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(OrderEvalute orderEvalute) {
        return getList(this.orderEvaluteService.queryByPage(orderEvalute));
    }


    /**
     * 新增数据
     *
     * @param orderEvalute 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(OrderEvalute orderEvalute) {
        return AjaxResult.ok(this.orderEvaluteService.insert(orderEvalute));
    }

    /**
     * 编辑数据
     *
     * @param orderEvalute 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(OrderEvalute orderEvalute) {
        return AjaxResult.ok(this.orderEvaluteService.update(orderEvalute));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.orderEvaluteService.deleteById(id));
    }

}

