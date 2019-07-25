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
package cn.com.xuxiaowei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 社交资源
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
@RequestMapping("/sns")
public class SnsRestController {

    private final DataSource dataSource;

    @SuppressWarnings("all")
    private final String USERINFO_BY_USERNAME = "SELECT username,sex,province,city,country,headimg_url FROM users WHERE username = ?";

    @Autowired
    public SnsRestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 根据 scope 获取用户详细信息的资源
     */
    @RequestMapping("/userinfo")
    public ResponseEntity<Map> userinfo(OAuth2Authentication oAuth2Authentication) {

        Map<String, Object> map = new HashMap<>(8);

        String name = oAuth2Authentication.getName();

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Set<String> scopes = oAuth2Request.getScope();

        boolean userinfo = scopes.contains("userinfo");
        if (userinfo) {
            map = userinfo(name);
        } else {
            map.put("username", name);
        }

        return ResponseEntity.ok(map);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名（主键）
     * @return 返回用户信息
     */
    @SuppressWarnings("all")
    private Map<String, Object> userinfo(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        return jdbcTemplate.queryForObject(USERINFO_BY_USERNAME,
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
    }

}
