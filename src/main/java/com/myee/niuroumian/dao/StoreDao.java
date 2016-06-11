package com.myee.niuroumian.dao;

import com.myee.niuroumian.domain.StoreInfo;
import com.myee.niuroumian.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jelynn on 2016/6/10.
 */
public interface StoreDao  extends JpaRepository<StoreInfo, Long> {
}
