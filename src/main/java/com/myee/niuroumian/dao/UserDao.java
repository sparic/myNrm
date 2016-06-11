package com.myee.niuroumian.dao;

import com.myee.niuroumian.domain.DishInfo;
import com.myee.niuroumian.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Ray.Fu on 2016/6/6.
 */
public interface UserDao extends JpaRepository<UserInfo, Long> {

    @Query("SELECT u FROM UserInfo u WHERE u.openId=:openId")
    public UserInfo findUserByOpenId(@Param("openId")String openId);

}
