package com.myee.niuroumian.service.impl;

import com.myee.niuroumian.dao.OrderInfoDao;
import com.myee.niuroumian.dao.StoreDao;
import com.myee.niuroumian.domain.StoreInfo;
import com.myee.niuroumian.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jelynn on 2016/6/10.
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Override
    public StoreInfo findById(Long storeId) {
        return storeDao.findOne(storeId);
    }
}
