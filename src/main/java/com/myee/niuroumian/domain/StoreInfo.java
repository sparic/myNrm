package com.myee.niuroumian.domain;

import javax.persistence.*;

/**
 * Created by Ray.Fu on 2016/6/1.
 */
@Entity
@Table(name = "t_store")
public class StoreInfo {
    @Id
    @Column(name = "store_id", columnDefinition = "INT")
    @TableGenerator(name = "PkGen_102", table = "ad_sequence", pkColumnName = "name", pkColumnValue = "T_Store", valueColumnName = "currentnextsys", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PkGen_102")
    private Long storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "user_id")
    private Long userId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
