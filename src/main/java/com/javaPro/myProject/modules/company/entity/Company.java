package com.javaPro.myProject.modules.company.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 销售商表(Company)实体类
 *

 */
public class Company implements Serializable {
    private static final long serialVersionUID = 435461577269146836L;

    private Integer id;
    /**
     * 手机号
     */
    private String phonenumber;
    /**
     * 注册地址
     */
    private String address;
    /**
     * 注册时间
     */
    private String zhuceshijian;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
     */
    private Integer createid;

    /**
     * 服务时间段（JSON格式存储）
     */
    private String serviceTime;

    /**
     * 平均评分
     */
    private Double avgRating;

    /**
     * 评价总数
     */
    private Integer ratingCount;

    /**
     * 服务区域
     */
    private String serviceArea;

    /**
     * 服务商用户名（用于显示，不存储在数据库中）
     */
    private String username;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZhuceshijian() {
        return zhuceshijian;
    }

    public void setZhuceshijian(String zhuceshijian) {
        this.zhuceshijian = zhuceshijian;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreateid() {
        return createid;
    }

    public void setCreateid(Integer createid) {
        this.createid = createid;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

