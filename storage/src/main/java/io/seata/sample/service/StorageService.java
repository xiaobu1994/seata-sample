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
public class StorageService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from storage_tbl");
        jdbcTemplate.update("insert into storage_tbl(commodity_code,count) values('C100000','200') ");
    }

    public void deduct(String commodityCode, int count) {
        jdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?",
                new Object[]{count, commodityCode});
    }

    public void deleteStorageTbl() {
        jdbcTemplate.update("delete  from storage_tbl ");
    }

    public Boolean validData(String commodityCode) {
        Map accountMap = jdbcTemplate.queryForMap("select * from storage_tbl where commodity_code=" + "'" + commodityCode + "'");
        return Integer.parseInt(accountMap.get("count").toString()) >= 0;
    }


}
