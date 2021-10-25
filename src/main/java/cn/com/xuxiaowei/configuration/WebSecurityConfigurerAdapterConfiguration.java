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
package cn.com.xuxiaowei.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Spring Security 配置
 * <p>
 * {@link EnableWebSecurity} 可省略
 *
 * @author xuxiaowei
 * @see org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration
 * @see org.springframework.security.config.web.server.SecurityWebFiltersOrder
 * @since 0.0.1
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapterConfiguration extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 用户密码编辑器
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 查询登录用户
        auth.userDetailsService(jdbcDaoImpl()).passwordEncoder(delegatingPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    /**
     * 登录时查询用户
     */
    public JdbcDaoImpl jdbcDaoImpl() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        return jdbcDao;
    }

}
