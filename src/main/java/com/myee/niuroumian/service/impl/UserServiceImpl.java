package com.myee.niuroumian.service.impl;

import com.myee.niuroumian.dao.UserDao;
import com.myee.niuroumian.domain.UserInfo;
import com.myee.niuroumian.service.UserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ray.Fu on 2016/6/6.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo saveOrUpdate(WxMpUser wxMpUser) {
        UserInfo user = new UserInfo();
        user.setOpenId(wxMpUser.getOpenId());
        user.setCity(wxMpUser.getCity());
        user.setCountry(wxMpUser.getCountry());
        user.setGroupId(wxMpUser.getGroupId());
        user.setHeadImgUrl(wxMpUser.getHeadImgUrl());
        user.setLanguage(wxMpUser.getLanguage());
        user.setNickName(wxMpUser.getNickname());
        user.setRemark(wxMpUser.getRemark());
        user.setSubscribe(wxMpUser.getSubscribe());
        user.setSex(wxMpUser.getSex());
        user.setSubscribeTime(new Date(wxMpUser.getSubscribeTime()/1000));
        user.setUnionId(wxMpUser.getUnionId());
        user.setSexId(wxMpUser.getSexId());
        //判断是否存在，是存在则更新
        if(userDao.findUserByOpenId(wxMpUser.getOpenId()) != null) {
            System.out.println("走这里1->");
            UserInfo oldUser = userDao.findUserByOpenId(wxMpUser.getOpenId());
            System.out.println("走这里2->" + oldUser.toString());
            oldUser.setOpenId(wxMpUser.getOpenId());
            oldUser.setCity(wxMpUser.getCity());
            oldUser.setCountry(wxMpUser.getCountry());
            oldUser.setGroupId(wxMpUser.getGroupId());
            oldUser.setHeadImgUrl(wxMpUser.getHeadImgUrl());
            oldUser.setLanguage(wxMpUser.getLanguage());
            oldUser.setNickName(wxMpUser.getNickname());
            oldUser.setRemark(wxMpUser.getRemark());
            oldUser.setSubscribe(wxMpUser.getSubscribe());
            oldUser.setSex(wxMpUser.getSex());
            oldUser.setSubscribeTime(new Date(new Date(wxMpUser.getSubscribeTime()).getTime() * 1000));
            oldUser.setUnionId(wxMpUser.getUnionId());
            oldUser.setSexId(wxMpUser.getSexId());
            System.out.println("走这里3->");
            userDao.save(oldUser);
        } else {
            //不存在则
            user = userDao.saveAndFlush(user);
        }
        return user;
    }

    @Override
    public UserInfo findUserByOpenId(String openId) {
        return userDao.findUserByOpenId(openId);
    }

    /**
     * @Description: long类型转换成日期
     *
     * @param lo 毫秒数
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

}
