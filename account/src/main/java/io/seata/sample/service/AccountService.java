package io.seata.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('U100000','10000') ");
    }

    public void reduce(String userId, int money) {
        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[]{money, userId});
    }


    public void deleteAccountTbl() {
        jdbcTemplate.update("delete  from account_tbl ");
    }

    public Boolean validData(String userId) {
        Map accountMap = jdbcTemplate.queryForMap("select * from account_tbl where user_id=" + "'" + userId + "'");
        return Integer.parseInt(accountMap.get("money").toString()) >= 0;
    }

}
