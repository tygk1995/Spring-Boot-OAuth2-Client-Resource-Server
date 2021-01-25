/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.xuxiaowei;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 社交资源 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@SpringBootTest
class SnsTests {

    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("all")
    private final String USERINFO_BY_USERNAME = "SELECT username,sex,province,city,country,headimg_url FROM users WHERE username = ?";

    private String username = "xuxiaowei";

    /**
     * 根据用户名，查询用户信息
     */
    @Test
    void userinfo() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        Map map = jdbcTemplate.queryForObject(USERINFO_BY_USERNAME,
                new String[]{username},
                new RowMapper<Map>() {
                    @Override
                    public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> userinfo = new HashMap<>(8);

                        userinfo.put("username", rs.getString(1));
                        userinfo.put("sex", rs.getInt(2));
                        userinfo.put("province", rs.getString(3));
                        userinfo.put("city", rs.getString(4));
                        userinfo.put("country", rs.getString(5));
                        userinfo.put("headimgUrl", rs.getString(6));

                        return userinfo;
                    }
                });

        System.err.println(map);
    }

}
