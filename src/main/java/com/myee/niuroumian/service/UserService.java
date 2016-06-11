package com.myee.niuroumian.service;

import com.myee.niuroumian.domain.UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.data.repository.query.Param;

/**
 * Created by Ray.Fu on 2016/6/6.
 */
public interface UserService {

    public UserInfo saveOrUpdate(WxMpUser wxMpUser);

    public UserInfo findUserByOpenId(String openId);
}
