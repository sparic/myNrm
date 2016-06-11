package com.myee.niuroumian.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ray.Fu on 2016/6/1.
 */
@Entity
@Table(name = "t_user")
public class UserInfo {
    @Id
    @Column(name = "user_id", columnDefinition = "INT")
    @TableGenerator(name = "PkGen_101", table = "ad_sequence", pkColumnName = "name", pkColumnValue = "T_User", valueColumnName = "currentnextsys", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PkGen_101")
    private Long userId;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile_num")
    private Long mobileNum;

    @Column(name = "subscribe")
    private Integer subscribe;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "language")
    private String language;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "head_img_url")
    private String headImgUrl;

    @Column(name = "subscribe_time")
    private Date subscribeTime;

    @Column(name = "union_id")
    private String unionId;

    @Column(name = "sex_id")
    private Integer sexId;

    @Column(name = "remark")
    private String remark;

    @Column(name = "group_id")
    private Integer groupId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Long mobileNum) {
        this.mobileNum = mobileNum;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        if(subscribe) {
            this.subscribe = 1;
        } else {
            this.subscribe = 0;
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getSexId() {
        return sexId;
    }

    public void setSexId(Integer sexId) {
        this.sexId = sexId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
