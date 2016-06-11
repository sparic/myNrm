package com.myee.niuroumian.dao;

import com.myee.niuroumian.domain.DishInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/6/1.
 */
public interface DishDao extends JpaRepository<DishInfo, Long> {

    @Query("SELECT d FROM DishInfo d WHERE d.storeId=:storeId")
    List<DishInfo> queryAllDishByStoreId(@Param("storeId")Long storeId);

}
