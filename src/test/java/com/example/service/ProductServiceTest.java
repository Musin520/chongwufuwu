package com.example.service;

import com.javaPro.myProject.modules.company.service.CompanyService;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.impl.ProductServiceImpl;
import com.javaPro.myProject.modules.userlike.dao.UserlikeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductDao productDao;
    
    @Mock
    private UserlikeDao userlikeDao;
    
    @Mock
    private CompanyService companyService;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    private Product testProduct;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setProductname("Test Product");
        testProduct.setKedanjia("99.99");
        testProduct.setImg("test-image.jpg");
        testProduct.setDetailimg("[\"detail1.jpg\", \"detail2.jpg\"]");
        testProduct.setCompanyid(1);
    }
    
    @Test
    void testQueryById_Success() {
        // Given
        when(productDao.queryById(1)).thenReturn(testProduct);
        
        // When
        Product result = productService.queryById(1);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getProductname()).isEqualTo("Test Product");
        assertThat(result.getDetailImgList()).hasSize(2);
        verify(productDao).queryById(1);
    }
    
    @Test
    void testQueryById_NotFound() {
        // Given
        when(productDao.queryById(999)).thenReturn(null);
        
        // When
        Product result = productService.queryById(999);
        
        // Then
        assertThat(result).isNull();
        verify(productDao).queryById(999);
    }
    
    @Test
    void testQueryByPage_Success() {
        // Given
        List<Product> productList = Arrays.asList(testProduct);
        when(productDao.queryAllByLimit(any(Product.class))).thenReturn(productList);
        
        // When
        List<Product> result = productService.queryByPage(new Product());
        
        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductname()).isEqualTo("Test Product");
        verify(productDao).queryAllByLimit(any(Product.class));
    }
    
    @Test
    void testInsert_Success() {
        // Given
        when(productDao.insert(any(Product.class))).thenReturn(1);
        
        // When
        Product result = productService.insert(testProduct);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductname()).isEqualTo("Test Product");
        verify(productDao).insert(testProduct);
    }
    
    @Test
    void testUpdate_Success() {
        // Given
        when(productDao.update(any(Product.class))).thenReturn(1);
        when(productDao.queryById(testProduct.getId())).thenReturn(testProduct);

        // When
        Product result = productService.update(testProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductname()).isEqualTo("Test Product");
        verify(productDao).update(testProduct);
        verify(productDao).queryById(testProduct.getId());
    }
    
    @Test
    void testDeleteById_Success() {
        // Given
        when(productDao.deleteById(anyInt())).thenReturn(1);
        
        // When
        boolean result = productService.deleteById(1);
        
        // Then
        assertThat(result).isTrue();
        verify(productDao).deleteById(1);
    }
    
    @Test
    void testDeleteById_Failed() {
        // Given
        when(productDao.deleteById(anyInt())).thenReturn(0);
        
        // When
        boolean result = productService.deleteById(999);
        
        // Then
        assertThat(result).isFalse();
        verify(productDao).deleteById(999);
    }
    
    @Test
    void testQueryById_WithNullDetailImg() {
        // Given
        testProduct.setDetailimg(null);
        when(productDao.queryById(1)).thenReturn(testProduct);
        
        // When
        Product result = productService.queryById(1);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDetailImgList()).isNull();
        verify(productDao).queryById(1);
    }
}
