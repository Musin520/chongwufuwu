package com.javaPro.myProject.modules.company.service.impl;

import com.javaPro.myProject.modules.company.dao.CompanyDao;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售商表(Company)表服务实现类
 *
 * @author
 * @since 07:49:22
 */
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {
    @Resource
    private CompanyDao companyDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Company queryById(Integer id) {
        return this.companyDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param company     筛选条件
     * @return 查询结果
     */
    @Override
    public List<Company> queryByPage(Company company) {

        return this.companyDao.queryAllByLimit(company);
    }

    /**
     * 新增数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    @Override
    public Company insert(Company company) {
        this.companyDao.insert(company);
        return company;
    }

    /**
     * 修改数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    @Override
    public Company update(Company company) {
        this.companyDao.update(company);
        return this.queryById(company.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.companyDao.deleteById(id) > 0;
    }
}
