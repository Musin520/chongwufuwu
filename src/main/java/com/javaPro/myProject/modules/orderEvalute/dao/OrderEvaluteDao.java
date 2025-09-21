package com.javaPro.myProject.modules.orderEvalute.dao;

import com.javaPro.myProject.modules.orderEvalute.entity.OrderEvalute;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * 评价表(OrderEvalute)表数据库访问层
 */
public interface OrderEvaluteDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OrderEvalute queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param orderEvalute 查询条件
     * @return 对象列表
     */
    List<OrderEvalute> queryAllByLimit(OrderEvalute orderEvalute);

    /**
     * 统计总行数
     *
     * @param orderEvalute 查询条件
     * @return 总行数
     */
    long count(OrderEvalute orderEvalute);

    /**
     * 新增数据
     *
     * @param orderEvalute 实例对象
     * @return 影响行数
     */
    int insert(OrderEvalute orderEvalute);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<OrderEvalute> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OrderEvalute> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<OrderEvalute> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<OrderEvalute> entities);

    /**
     * 修改数据
     *
     * @param orderEvalute 实例对象
     * @return 影响行数
     */
    int update(OrderEvalute orderEvalute);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

