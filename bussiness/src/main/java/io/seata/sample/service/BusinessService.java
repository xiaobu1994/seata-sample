package io.seata.sample.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import io.seata.sample.feign.AccountFeignClient;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount) {
        storageFeignClient.deduct(commodityCode, orderCount);

        orderFeignClient.create(userId, commodityCode, orderCount);

        if (!validData(userId, commodityCode)) {
            throw new RuntimeException("账户或库存不足,执行回滚");
        }
    }



    public boolean validData(String userId, String commodityCode) {
        boolean flag = accountFeignClient.validData(userId);
        if (!flag) {
            return flag;
        }
        flag = storageFeignClient.validData(commodityCode);
        if (!flag) {
            return flag;
        }

        return true;
    }
}
