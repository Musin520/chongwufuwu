package com.javaPro.myProject.modules.company.dao;

import com.javaPro.myProject.modules.company.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售商表(Company)表数据库访问层
 *
 * @author
 * @since 07:49:21
 */
public interface CompanyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Company queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param company 查询条件
     * @return 对象列表
     */
    List<Company> queryAllByLimit(Company company);

    /**
     * 统计总行数
     *
     * @param company 查询条件
     * @return 总行数
     */
    long count(Company company);

    /**
     * 新增数据
     *
     * @param company 实例对象
     * @return 影响行数
     */
    int insert(Company company);

    /**
     * 新增指定ID的数据
     *
     * @param company 实例对象
     * @return 影响行数
     */
    int insertWithId(Company company);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Company> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Company> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Company> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Company> entities);

    /**
     * 修改数据
     *
     * @param company 实例对象
     * @return 影响行数
     */
    int update(Company company);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据创建者ID查询服务商信息
     *
     * @param createid 创建者ID（用户ID）
     * @return 服务商信息
     */
    Company queryByCreateId(Integer createid);

}

