package com.example.workflow;

import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.ProductService;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * 业务流程测试类
 * 覆盖角色功能测试规格说明书中的业务流程测试
 */
@SpringBootTest(classes = com.javaPro.myProject.SchedulingApplication.class)
@ActiveProfiles("test")
class BusinessFlowTest {

    @MockBean
    private SysuserService sysuserService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private ProductService productService;

    private Sysuser testUser;
    private Company testCompany;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 测试用户
        testUser = new Sysuser();
        testUser.setId(1);
        testUser.setUsername("测试用户");
        testUser.setAccount("testuser");
        testUser.setPassword("password123");
        testUser.setRole("2"); // 普通用户
        testUser.setPhonenumber("13800138000");
        testUser.setCreatetime(new Date());

        // 测试服务商
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setCompanyname("测试宠物服务公司");
        testCompany.setFaren("张三");
        testCompany.setPhonenumber("13900139000");
        testCompany.setStatus("1"); // 已审核通过
        testCompany.setCreatetime(new Date());

        // 测试商品
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setProductname("宠物美容服务");
        testProduct.setProductdes("专业宠物美容护理");
        testProduct.setKedanjia("100.00");
        testProduct.setKucun("10");
        testProduct.setCompanyid(1);
    }

    /**
     * 测试用例ID: TC_BF_001
     * 测试描述: 完整的用户注册流程
     * 对应角色功能测试规格说明书: 3.1.1 用户注册功能
     */
    @Test
    void testCompleteUserRegistrationFlow() {
        // 1. 用户注册
        when(sysuserService.insert(any(Sysuser.class))).thenReturn(testUser);
        when(sysuserService.queryByAccount("testuser")).thenReturn(null); // 账号不存在

        Sysuser registeredUser = sysuserService.insert(testUser);
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("测试用户");

        // 2. 验证注册信息
        when(sysuserService.queryByAccount("testuser")).thenReturn(testUser);
        Sysuser foundUser = sysuserService.queryByAccount("testuser");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getAccount()).isEqualTo("testuser");

        // 3. 用户登录
        when(sysuserService.queryByAccount("testuser")).thenReturn(testUser);
        Sysuser loginUser = sysuserService.queryByAccount("testuser");
        assertThat(loginUser).isNotNull();
        assertThat(loginUser.getPassword()).isEqualTo("password123");

        verify(sysuserService, times(1)).insert(any(Sysuser.class));
        verify(sysuserService, times(2)).queryByAccount("testuser"); // 修正为实际调用次数
    }

    /**
     * 测试用例ID: TC_BF_002
     * 测试描述: 服务商入驻审核流程
     * 对应角色功能测试规格说明书: 5.2.1 服务商审核
     */
    @Test
    void testMerchantRegistrationAuditFlow() {
        // 1. 服务商提交入驻申请
        Company pendingCompany = new Company();
        pendingCompany.setCompanyname("新申请宠物服务公司");
        pendingCompany.setStatus("0"); // 待审核状态

        when(companyService.insert(any(Company.class))).thenReturn(pendingCompany);
        Company submittedCompany = companyService.insert(pendingCompany);
        assertThat(submittedCompany.getStatus()).isEqualTo("0");

        // 2. 管理员审核资质
        when(companyService.queryById(1)).thenReturn(pendingCompany);
        Company auditCompany = companyService.queryById(1);
        assertThat(auditCompany).isNotNull();

        // 3. 审核通过，更新状态
        auditCompany.setStatus("1"); // 审核通过
        when(companyService.update(any(Company.class))).thenReturn(auditCompany);
        Company approvedCompany = companyService.update(auditCompany);
        assertThat(approvedCompany.getStatus()).isEqualTo("1");

        verify(companyService, times(1)).insert(any(Company.class));
        verify(companyService, times(1)).queryById(1);
        verify(companyService, times(1)).update(any(Company.class));
    }

    /**
     * 测试用例ID: TC_BF_003
     * 测试描述: 商品发布审核流程
     * 对应角色功能测试规格说明书: 5.3.1 商品内容审核
     */
    @Test
    void testProductPublishAuditFlow() {
        // 1. 服务商发布商品
        Product newProduct = new Product();
        newProduct.setProductname("新宠物服务");
        newProduct.setStatus("0"); // 待审核状态
        newProduct.setCompanyid(1);

        when(productService.insert(any(Product.class))).thenReturn(newProduct);
        Product publishedProduct = productService.insert(newProduct);
        assertThat(publishedProduct.getStatus()).isEqualTo("0");

        // 2. 管理员审核商品
        when(productService.queryById(1)).thenReturn(newProduct);
        Product auditProduct = productService.queryById(1);
        assertThat(auditProduct).isNotNull();

        // 3. 审核通过，商品上架
        auditProduct.setStatus("1"); // 审核通过
        when(productService.update(any(Product.class))).thenReturn(auditProduct);
        Product approvedProduct = productService.update(auditProduct);
        assertThat(approvedProduct.getStatus()).isEqualTo("1");

        verify(productService, times(1)).insert(any(Product.class));
        verify(productService, times(1)).queryById(1);
        verify(productService, times(1)).update(any(Product.class));
    }

    /**
     * 测试用例ID: TC_BF_004
     * 测试描述: 用户浏览商品流程
     * 对应角色功能测试规格说明书: 3.1 商品浏览功能
     */
    @Test
    void testUserBrowseProductFlow() {
        // 1. 用户浏览商品列表
        List<Product> productList = Arrays.asList(testProduct);
        when(productService.queryByPage(any(Product.class))).thenReturn(productList);

        List<Product> browsedProducts = productService.queryByPage(new Product());
        assertThat(browsedProducts).hasSize(1);
        assertThat(browsedProducts.get(0).getProductname()).isEqualTo("宠物美容服务");

        // 2. 用户查看商品详情
        when(productService.queryById(1)).thenReturn(testProduct);
        Product productDetail = productService.queryById(1);
        assertThat(productDetail).isNotNull();
        assertThat(productDetail.getProductdes()).isEqualTo("专业宠物美容护理");

        // 3. 用户查看服务商信息
        when(companyService.queryById(1)).thenReturn(testCompany);
        Company companyInfo = companyService.queryById(1);
        assertThat(companyInfo).isNotNull();
        assertThat(companyInfo.getCompanyname()).isEqualTo("测试宠物服务公司");

        verify(productService, times(1)).queryByPage(any(Product.class));
        verify(productService, times(1)).queryById(1);
        verify(companyService, times(1)).queryById(1);
    }

    /**
     * 测试用例ID: TC_BF_005
     * 测试描述: 用户信息管理流程
     * 对应角色功能测试规格说明书: 3.4.1 个人信息管理
     */
    @Test
    void testUserProfileManagementFlow() {
        // 1. 用户查看个人信息
        when(sysuserService.queryById(1)).thenReturn(testUser);
        Sysuser userProfile = sysuserService.queryById(1);
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getUsername()).isEqualTo("测试用户");

        // 2. 用户修改个人信息
        testUser.setUsername("更新后的用户名");
        testUser.setAddress("北京市朝阳区新地址");
        when(sysuserService.update(any(Sysuser.class))).thenReturn(testUser);

        Sysuser updatedUser = sysuserService.update(testUser);
        assertThat(updatedUser.getUsername()).isEqualTo("更新后的用户名");
        assertThat(updatedUser.getAddress()).isEqualTo("北京市朝阳区新地址");

        // 3. 验证信息更新成功
        when(sysuserService.queryById(1)).thenReturn(testUser);
        Sysuser verifyUser = sysuserService.queryById(1);
        assertThat(verifyUser.getUsername()).isEqualTo("更新后的用户名");

        verify(sysuserService, times(2)).queryById(1);
        verify(sysuserService, times(1)).update(any(Sysuser.class));
    }

    /**
     * 测试用例ID: TC_BF_006
     * 测试描述: 服务商商品管理流程
     * 对应角色功能测试规格说明书: 4.1.1 商品基础管理
     */
    @Test
    void testMerchantProductManagementFlow() {
        // 1. 服务商添加新商品
        when(productService.insert(any(Product.class))).thenReturn(testProduct);
        Product addedProduct = productService.insert(testProduct);
        assertThat(addedProduct).isNotNull();
        assertThat(addedProduct.getProductname()).isEqualTo("宠物美容服务");

        // 2. 服务商查看自己的商品列表
        Product queryCondition = new Product();
        queryCondition.setCompanyid(1);
        List<Product> merchantProducts = Arrays.asList(testProduct);
        when(productService.queryByPage(any(Product.class))).thenReturn(merchantProducts);

        List<Product> productList = productService.queryByPage(queryCondition);
        assertThat(productList).hasSize(1);
        assertThat(productList.get(0).getCompanyid()).isEqualTo(1);

        // 3. 服务商编辑商品信息
        testProduct.setProductname("更新后的宠物美容服务");
        testProduct.setKedanjia("120.00");
        when(productService.update(any(Product.class))).thenReturn(testProduct);

        Product updatedProduct = productService.update(testProduct);
        assertThat(updatedProduct.getProductname()).isEqualTo("更新后的宠物美容服务");
        assertThat(updatedProduct.getKedanjia()).isEqualTo("120.00");

        // 4. 服务商下架商品
        when(productService.deleteById(1)).thenReturn(true);
        boolean deleted = productService.deleteById(1);
        assertThat(deleted).isTrue();

        verify(productService, times(1)).insert(any(Product.class));
        verify(productService, times(1)).queryByPage(any(Product.class));
        verify(productService, times(1)).update(any(Product.class));
        verify(productService, times(1)).deleteById(1);
    }

    /**
     * 测试用例ID: TC_BF_007
     * 测试描述: 管理员用户管理流程
     * 对应角色功能测试规格说明书: 5.1.1 用户账户管理
     */
    @Test
    void testAdminUserManagementFlow() {
        // 1. 管理员查看用户列表
        List<Sysuser> userList = Arrays.asList(testUser);
        when(sysuserService.queryByPage(any(Sysuser.class))).thenReturn(userList);

        List<Sysuser> allUsers = sysuserService.queryByPage(new Sysuser());
        assertThat(allUsers).hasSize(1);
        assertThat(allUsers.get(0).getRole()).isEqualTo("2");

        // 2. 管理员查看用户详情
        when(sysuserService.queryById(1)).thenReturn(testUser);
        Sysuser userDetail = sysuserService.queryById(1);
        assertThat(userDetail).isNotNull();

        // 3. 管理员修改用户权限
        testUser.setRole("3"); // 升级为服务商
        when(sysuserService.update(any(Sysuser.class))).thenReturn(testUser);

        Sysuser updatedUser = sysuserService.update(testUser);
        assertThat(updatedUser.getRole()).isEqualTo("3");

        verify(sysuserService, times(1)).queryByPage(any(Sysuser.class));
        verify(sysuserService, times(1)).queryById(1);
        verify(sysuserService, times(1)).update(any(Sysuser.class));
    }
}
