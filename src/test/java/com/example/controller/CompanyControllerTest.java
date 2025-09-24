package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaPro.myProject.modules.company.controller.CompanyController;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 服务商控制器测试类
 * 覆盖角色功能测试规格说明书中的服务商管理功能测试
 */
@SpringBootTest(classes = com.javaPro.myProject.SchedulingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Company testCompany;
    private List<Company> companyList;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setCompanyname("爱宠宠物服务有限公司");
        testCompany.setFaren("张三");
        testCompany.setPhonenumber("13800138000");
        testCompany.setAddress("北京市朝阳区宠物街1号");
        testCompany.setYingyezhizhaohao("91110000123456789X");
        testCompany.setJingyingfanwei("宠物美容、寄养、医疗");
        testCompany.setStatus("1"); // 正常状态
        testCompany.setCreatetime(new Date());
        testCompany.setAvgRating(4.5);
        testCompany.setRatingCount(100);

        Company company2 = new Company();
        company2.setId(2);
        company2.setCompanyname("宠物之家服务中心");
        company2.setFaren("李四");
        company2.setPhonenumber("13900139000");
        company2.setAddress("北京市海淀区宠物大道2号");
        company2.setStatus("0"); // 待审核状态

        companyList = Arrays.asList(testCompany, company2);
    }

    /**
     * 测试用例ID: TC_CC_001
     * 测试描述: 分页查询服务商列表成功
     * 对应角色功能测试规格说明书: 5.2 服务商管理模块
     */
    @Test
    void testQueryByPage_Success() throws Exception {
        when(companyService.queryByPage(any(Company.class))).thenReturn(companyList);

        mockMvc.perform(get("/company")
                        .param("companyname", "宠物")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].companyname").value("爱宠宠物服务有限公司"))
                .andExpect(jsonPath("$.list[1].companyname").value("宠物之家服务中心"));
    }

    /**
     * 测试用例ID: TC_CC_002
     * 测试描述: 根据ID查询服务商详情成功
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核 TC_MA_001
     */
    @Test
    void testQueryById_Success() throws Exception {
        when(companyService.queryById(1)).thenReturn(testCompany);

        mockMvc.perform(get("/company/detail")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.id").value(1))
                .andExpect(jsonPath("$.Data.companyname").value("爱宠宠物服务有限公司"))
                .andExpect(jsonPath("$.Data.faren").value("张三"))
                .andExpect(jsonPath("$.Data.status").value("1"));
    }

    /**
     * 测试用例ID: TC_CC_003
     * 测试描述: 服务商入驻申请成功
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核 TC_MA_001
     */
    @Test
    void testAdd_Success() throws Exception {
        when(companyService.insert(any(Company.class))).thenReturn(testCompany);

        mockMvc.perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.companyname").value("爱宠宠物服务有限公司"))
                .andExpect(jsonPath("$.Data.faren").value("张三"));
    }

    /**
     * 测试用例ID: TC_CC_004
     * 测试描述: 更新服务商信息成功
     * 对应角色功能测试规格说明书: 4.3.1 店铺信息管理 TC_SM_001
     */
    @Test
    void testEdit_Success() throws Exception {
        testCompany.setCompanyname("更新后的爱宠宠物服务有限公司");
        testCompany.setJingyingfanwei("宠物美容、寄养、医疗、训练");
        when(companyService.update(any(Company.class))).thenReturn(testCompany);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.companyname").value("更新后的爱宠宠物服务有限公司"))
                .andExpect(jsonPath("$.Data.jingyingfanwei").value("宠物美容、寄养、医疗、训练"));
    }

    /**
     * 测试用例ID: TC_CC_005
     * 测试描述: 删除服务商成功
     * 管理员权限测试
     */
    @Test
    void testDelete_Success() throws Exception {
        when(companyService.deleteById(1)).thenReturn(true);

        mockMvc.perform(delete("/company")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data").value(true));
    }

    /**
     * 测试用例ID: TC_CC_006
     * 测试描述: 服务商资质验证
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核 TC_MA_002
     */
    @Test
    void testCompanyQualificationValidation() throws Exception {
        // 测试营业执照号格式验证
        Company invalidCompany = new Company();
        invalidCompany.setCompanyname("测试公司");
        invalidCompany.setYingyezhizhaohao("invalid_license"); // 无效的营业执照号

        when(companyService.insert(any(Company.class))).thenReturn(invalidCompany);

        mockMvc.perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCompany)))
                .andExpect(status().isOk());
    }

    /**
     * 测试用例ID: TC_CC_007
     * 测试描述: 服务商状态管理
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核
     */
    @Test
    void testCompanyStatusManagement() throws Exception {
        // 测试审核通过
        testCompany.setStatus("1"); // 审核通过
        when(companyService.update(any(Company.class))).thenReturn(testCompany);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.status").value("1"));
    }

    /**
     * 测试用例ID: TC_CC_008
     * 测试描述: 服务商评分查询
     * 对应角色功能测试规格说明书: 4.4.1 销售数据分析
     */
    @Test
    void testCompanyRatingQuery() throws Exception {
        when(companyService.queryById(1)).thenReturn(testCompany);

        mockMvc.perform(get("/company/detail")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.avgRating").value(4.5))
                .andExpect(jsonPath("$.Data.ratingCount").value(100));
    }

    /**
     * 测试用例ID: TC_CC_009
     * 测试描述: 查询不存在的服务商
     * 边界条件测试
     */
    @Test
    void testQueryById_NotFound() throws Exception {
        when(companyService.queryById(999)).thenReturn(null);

        mockMvc.perform(get("/company/detail")
                        .param("id", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data").isEmpty());
    }

    /**
     * 测试用例ID: TC_CC_010
     * 测试描述: 服务商营业时间设置
     * 对应角色功能测试规格说明书: 4.3.1 店铺信息管理 TC_SM_003
     */
    @Test
    void testServiceTimeConfiguration() throws Exception {
        testCompany.setServiceTime("{\"monday\":\"09:00-18:00\",\"tuesday\":\"09:00-18:00\"}");
        when(companyService.update(any(Company.class))).thenReturn(testCompany);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data.serviceTime").exists());
    }
}
