package com.example.service;

import com.javaPro.myProject.modules.company.dao.CompanyDao;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * 服务商服务层测试类
 * 覆盖测试规格说明书中的服务层测试要求
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyDao companyDao;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company testCompany;
    private List<Company> companyList;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setCompanyname("测试宠物服务公司");
        testCompany.setFaren("张三");
        testCompany.setPhonenumber("13800138000");
        testCompany.setAddress("北京市朝阳区测试街1号");
        testCompany.setYingyezhizhaohao("91110000123456789X");
        testCompany.setJingyingfanwei("宠物美容、寄养、医疗");
        testCompany.setStatus("1");
        testCompany.setCreatetime(new Date());
        testCompany.setAvgRating(4.5);
        testCompany.setRatingCount(100);

        Company company2 = new Company();
        company2.setId(2);
        company2.setCompanyname("另一家宠物服务公司");
        company2.setFaren("李四");
        company2.setStatus("0"); // 待审核

        companyList = Arrays.asList(testCompany, company2);
    }

    /**
     * 测试用例ID: TC_CS_001
     * 测试描述: 根据ID查询服务商成功
     * 对应测试规格说明书: 4.2.1 服务层单元测试
     */
    @Test
    void testQueryById_Success() {
        when(companyDao.queryById(1)).thenReturn(testCompany);

        Company result = companyService.queryById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCompanyname()).isEqualTo("测试宠物服务公司");
        assertThat(result.getFaren()).isEqualTo("张三");
        verify(companyDao, times(1)).queryById(1);
    }

    /**
     * 测试用例ID: TC_CS_002
     * 测试描述: 查询不存在的服务商
     * 边界条件测试
     */
    @Test
    void testQueryById_NotFound() {
        when(companyDao.queryById(999)).thenReturn(null);

        Company result = companyService.queryById(999);

        assertThat(result).isNull();
        verify(companyDao, times(1)).queryById(999);
    }

    /**
     * 测试用例ID: TC_CS_003
     * 测试描述: 分页查询服务商列表成功
     * 对应测试规格说明书: 4.2.1 服务层单元测试
     */
    @Test
    void testQueryByPage_Success() {
        Company queryCondition = new Company();
        queryCondition.setCompanyname("宠物");

        when(companyDao.queryAllByLimit(any(Company.class))).thenReturn(companyList);

        List<Company> result = companyService.queryByPage(queryCondition);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCompanyname()).isEqualTo("测试宠物服务公司");
        assertThat(result.get(1).getCompanyname()).isEqualTo("另一家宠物服务公司");
        verify(companyDao, times(1)).queryAllByLimit(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_004
     * 测试描述: 新增服务商成功
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核 TC_MA_001
     */
    @Test
    void testInsert_Success() {
        when(companyDao.insert(any(Company.class))).thenReturn(1);

        Company result = companyService.insert(testCompany);

        assertThat(result).isNotNull();
        assertThat(result.getCompanyname()).isEqualTo("测试宠物服务公司");
        assertThat(result.getFaren()).isEqualTo("张三");
        verify(companyDao, times(1)).insert(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_005
     * 测试描述: 更新服务商信息成功
     * 对应角色功能测试规格说明书: 4.3.1 店铺信息管理 TC_SM_001
     */
    @Test
    void testUpdate_Success() {
        testCompany.setCompanyname("更新后的公司名称");
        testCompany.setJingyingfanwei("宠物美容、寄养、医疗、训练");

        when(companyDao.update(any(Company.class))).thenReturn(1);
        when(companyDao.queryById(testCompany.getId())).thenReturn(testCompany);

        Company result = companyService.update(testCompany);

        assertThat(result).isNotNull();
        assertThat(result.getCompanyname()).isEqualTo("更新后的公司名称");
        assertThat(result.getJingyingfanwei()).isEqualTo("宠物美容、寄养、医疗、训练");
        verify(companyDao, times(1)).update(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_006
     * 测试描述: 删除服务商成功
     * 管理员权限测试
     */
    @Test
    void testDeleteById_Success() {
        when(companyDao.deleteById(1)).thenReturn(1);

        boolean result = companyService.deleteById(1);

        assertThat(result).isTrue();
        verify(companyDao, times(1)).deleteById(1);
    }

    /**
     * 测试用例ID: TC_CS_007
     * 测试描述: 删除不存在的服务商
     * 边界条件测试
     */
    @Test
    void testDeleteById_NotFound() {
        when(companyDao.deleteById(999)).thenReturn(0);

        boolean result = companyService.deleteById(999);

        assertThat(result).isFalse();
        verify(companyDao, times(1)).deleteById(999);
    }

    /**
     * 测试用例ID: TC_CS_008
     * 测试描述: 服务商状态管理
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核
     */
    @Test
    void testCompanyStatusManagement() {
        // 测试审核通过
        testCompany.setStatus("1"); // 审核通过
        when(companyDao.update(any(Company.class))).thenReturn(1);
        when(companyDao.queryById(testCompany.getId())).thenReturn(testCompany);

        Company result = companyService.update(testCompany);

        assertThat(result.getStatus()).isEqualTo("1");
        verify(companyDao, times(1)).update(any(Company.class));

        // 测试审核拒绝
        testCompany.setStatus("2"); // 审核拒绝
        when(companyDao.update(any(Company.class))).thenReturn(1);
        when(companyDao.queryById(testCompany.getId())).thenReturn(testCompany);

        Company rejectedResult = companyService.update(testCompany);

        assertThat(rejectedResult.getStatus()).isEqualTo("2");
        verify(companyDao, times(2)).update(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_009
     * 测试描述: 服务商评分更新
     * 对应角色功能测试规格说明书: 4.4.1 销售数据分析
     */
    @Test
    void testUpdateCompanyRating() {
        testCompany.setAvgRating(4.8);
        testCompany.setRatingCount(150);

        when(companyDao.update(any(Company.class))).thenReturn(1);
        when(companyDao.queryById(testCompany.getId())).thenReturn(testCompany);

        Company result = companyService.update(testCompany);

        assertThat(result.getAvgRating()).isEqualTo(4.8);
        assertThat(result.getRatingCount()).isEqualTo(150);
        verify(companyDao, times(1)).update(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_010
     * 测试描述: 服务商营业时间设置
     * 对应角色功能测试规格说明书: 4.3.1 店铺信息管理 TC_SM_003
     */
    @Test
    void testServiceTimeConfiguration() {
        String serviceTime = "{\"monday\":\"09:00-18:00\",\"tuesday\":\"09:00-18:00\",\"wednesday\":\"09:00-18:00\"}";
        testCompany.setServiceTime(serviceTime);

        when(companyDao.update(any(Company.class))).thenReturn(1);
        when(companyDao.queryById(testCompany.getId())).thenReturn(testCompany);

        Company result = companyService.update(testCompany);

        assertThat(result.getServiceTime()).isEqualTo(serviceTime);
        verify(companyDao, times(1)).update(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_011
     * 测试描述: 服务商资质验证
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核 TC_MA_002
     */
    @Test
    void testCompanyQualificationValidation() {
        // 测试有效的营业执照号
        testCompany.setYingyezhizhaohao("91110000123456789X");
        when(companyDao.insert(any(Company.class))).thenReturn(1);

        Company result = companyService.insert(testCompany);

        assertThat(result.getYingyezhizhaohao()).isEqualTo("91110000123456789X");
        verify(companyDao, times(1)).insert(any(Company.class));
    }

    /**
     * 测试用例ID: TC_CS_012
     * 测试描述: 按状态查询服务商
     * 管理功能测试
     */
    @Test
    void testQueryByStatus() {
        Company statusQuery = new Company();
        statusQuery.setStatus("1"); // 查询已审核通过的服务商

        List<Company> approvedCompanies = Arrays.asList(testCompany);
        when(companyDao.queryAllByLimit(any(Company.class))).thenReturn(approvedCompanies);

        List<Company> result = companyService.queryByPage(statusQuery);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("1");
        verify(companyDao, times(1)).queryAllByLimit(any(Company.class));
    }
}
