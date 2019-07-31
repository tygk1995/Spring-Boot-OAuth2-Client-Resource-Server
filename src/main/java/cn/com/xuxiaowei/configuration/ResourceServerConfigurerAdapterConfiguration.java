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

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 *
 * @author xuxiaowei
 * @see <a href="http://127.0.0.1:8080/sns/userinfo?access_token=">访问资源</a>
 * @see org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration
 * @see org.springframework.security.config.web.server.SecurityWebFiltersOrder
 * @since 0.0.1
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurerAdapterConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

    }

    /**
     * {@link ResourceServerConfigurerAdapter}需要使用{@link EnableResourceServer}，
     * 在{@link EnableResourceServer}中使用了{@link ResourceServerConfiguration}，
     * 而{@link ResourceServerConfiguration}的{@link Order}为3，
     * 故{@link ResourceServerConfigurerAdapter}的{@link Order}也为3。
     * <p>
     * 而{@link WebSecurityConfigurerAdapter}的{@link Order}为100，
     * 同时使用{@link WebSecurityConfigurerAdapter}和{@link ResourceServerConfigurerAdapter}时，
     * 需要为{@link ResourceServerConfigurerAdapter#configure(HttpSecurity)}配置<code>http.antMatcher("/sns/**")</code>，
     * 否则{@link WebSecurityConfigurerAdapter}优先级低，将不起作用。
     * <p>
     * 注意：
     * 使用{@link Order}调整{@link WebSecurityConfigurerAdapter}、{@link ResourceServerConfigurerAdapter}的优先级也可行，
     * 但是不推荐，有风险。
     * 需要某些路径具有某些权限（特性）就直接去配置它们，不要牵扯别的模块。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置资源路径 /sns/** 需要的权限 scope
        http.antMatcher("/sns/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/sns/userinfo").access("#oauth2.hasAnyScope('base','userinfo')")
                .antMatchers(HttpMethod.POST, "/sns/userinfo").access("#oauth2.hasAnyScope('base','userinfo')")
        ;
    }

}
