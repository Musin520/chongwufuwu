package com.javaPro.myProject.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.ProductService;
import com.javaPro.myProject.modules.userlike.dao.UserlikeDao;
import com.javaPro.myProject.modules.userlike.entity.Userlike;
import com.javaPro.myProject.modules.company.service.CompanyService;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物服务表(Product)表服务实现类
 *
 * @author AjaxResult
 * @since AjaxResult 07:51:55
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;
    @Resource
    private UserlikeDao userlikeDao;
    @Resource
    private CompanyService companyService;
    @Resource
    private SysuserService sysuserService;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Product queryById(Integer id) {
        Product product = this.productDao.queryById(id);
        if (product != null) {
            // 处理详情图片
            if (product.getDetailimg() != null) {
                List<String> list = JSON.parseArray(product.getDetailimg(), String.class);
                System.out.println("list = " + list);
                product.setDetailImgList(list);
            }

            // 获取服务商信息
            if (product.getCompanyid() != null) {
                System.out.println("Product companyid: " + product.getCompanyid());
                Company company = companyService.queryById(product.getCompanyid());
                if (company != null) {
                    System.out.println("Found company with ID: " + company.getId());

                    // 通过createid获取用户信息，显示用户名作为服务商名称
                    if (company.getCreateid() != null) {
                        Sysuser user = sysuserService.queryById(company.getCreateid());
                        if (user != null && user.getUsername() != null) {
                            product.setCompanyName(user.getUsername());
                        } else {
                            product.setCompanyName("服务商" + company.getId());
                        }
                    } else {
                        product.setCompanyName("服务商" + company.getId());
                    }

                    product.setCompanyRating(company.getAvgRating());
                    product.setCompanyRatingCount(company.getRatingCount());
                    product.setCompanyServiceArea(company.getServiceArea());
                } else {
                    System.out.println("Company not found for id: " + product.getCompanyid());
                }
            } else {
                System.out.println("Product companyid is null");
            }
        }
        return product;
    }

    /**
     * 分页查询
     *
     * @param product 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Product> queryByPage(Product product) {
        List<Product> products = this.productDao.queryAllByLimit(product);
        if (!CollectionUtils.isEmpty(products)){
            for (Product item : products) {
                // 始终设置detailImgList
                if (item.getDetailimg() != null) {
                    item.setDetailImgList(JSON.parseArray(item.getDetailimg(), String.class));
                }

                // 只有当userid不为null时才处理收藏逻辑
                if (product.getUserid() != null) {
                    Userlike userlike = new Userlike();
                    userlike.setProductid(item.getId());
                    userlike.setUserid(Integer.valueOf(product.getUserid()));
                    List<Userlike> userlikes = userlikeDao.queryAllByLimit(userlike);
                    item.setLikeFlag(!CollectionUtils.isEmpty(userlikes));
                }
            }
        }
        return products;
    }

    /**
     * 新增数据
     *
     * @param product 实例对象
     * @return 实例对象
     */
    @Override
    public Product insert(Product product) {
        if (product.getImg()!= null){
            List<String> list = Arrays.asList(product.getImg().split(","));
            for (String s : list) {
                System.out.println("s = " + s);
            }
            product.setImg(list.get(0));
            product.setDetailimg(JSON.toJSONString(list)  );
        }
        this.productDao.insert(product);
        return product;
    }

    /**
     * 修改数据
     *
     * @param product 实例对象
     * @return 实例对象
     */
    @Override
    public Product update(Product product) {
        if (product.getImg()!= null){
            List<String> list = Arrays.asList(product.getImg().split(","));
            product.setImg(list.get(0));
            product.setDetailimg(JSON.toJSONString(list)  );
        }
        this.productDao.update(product);
        return this.queryById(product.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.productDao.deleteById(id) > 0;
    }

    /**
     * 更新产品服务时间段数据（用于测试）
     *
     * @return 更新的记录数
     */
    @Override
    public int updateServiceTimeForTesting() {
        return this.productDao.updateServiceTimeForTesting();
    }
}
