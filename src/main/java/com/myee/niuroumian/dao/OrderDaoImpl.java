package com.myee.niuroumian.dao;

import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/6/8.
 */
//@Component("orderDao")
public class OrderDaoImpl {

//    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> queryCustomerOrderByShop(String openId, Long shopId, Long start, Long end) {
        String sql = "SELECT r2.order_id FROM\n" +
                "\tr_order_item_info r1 LEFT JOIN r_order_info r2 on r1.order_id = r2.order_id\n" +
                "\tLEFT JOIN t_dish t on r1.dish_id = t.dish_id\n" +
                "\tLEFT JOIN t_user u on r2.user_id = u.open_id\n" +
                "where r2.pay_state = 1 and r2.user_id =" + openId + " and r2.shop_id=" + shopId + " ORDER BY r1.create_time desc LIMIT " + start + "," + end;
        List<Long> list = jdbcTemplate.query(sql, new Object[]{}, new OrderMapper());
        return list;
    }

    @SuppressWarnings("unchecked")
    private class OrderMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            OrderInfo vo = new OrderInfo();
            vo.setRemark(rs.getString("remark"));
            vo.setCount(rs.getInt("count"));
            vo.setCreateTime(TimeUtil.convertStr2Tim(rs.getString("create_time")));
            return vo;
        }
    }
}
