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
     * 销售商名称
     */
    private String companyname;
    /**
     * 执照照片
     */
    private String img;
    /**
     * 营业执照统一信贷码
     */
    private String yingyezhizhaohao;
    /**
     * 法人
     */
    private String faren;
    /**
     * 手机号
     */
    private String phonenumber;
    /**
     * 经营范围
     */
    private String jingyingfanwei;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getYingyezhizhaohao() {
        return yingyezhizhaohao;
    }

    public void setYingyezhizhaohao(String yingyezhizhaohao) {
        this.yingyezhizhaohao = yingyezhizhaohao;
    }

    public String getFaren() {
        return faren;
    }

    public void setFaren(String faren) {
        this.faren = faren;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getJingyingfanwei() {
        return jingyingfanwei;
    }

    public void setJingyingfanwei(String jingyingfanwei) {
        this.jingyingfanwei = jingyingfanwei;
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

}

