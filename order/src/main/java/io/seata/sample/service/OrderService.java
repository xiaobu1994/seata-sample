package io.seata.sample.service;

import io.seata.sample.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class OrderService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from order_tbl");
    }

    public void create(String userId, String commodityCode, Integer count) {

        int orderMoney = count * 100;
        jdbcTemplate.update("insert order_tbl(user_id,commodity_code,count,money) values(?,?,?,?)",
            new Object[] {userId, commodityCode, count, orderMoney});

        userFeignClient.reduce(userId, orderMoney);
    }


    public void deleteOrderTbl() {
        jdbcTemplate.update("delete  from order_tbl ");
    }

}
