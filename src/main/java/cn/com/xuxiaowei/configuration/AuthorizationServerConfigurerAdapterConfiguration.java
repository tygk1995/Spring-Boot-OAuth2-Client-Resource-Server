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
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * 授权服务器配置
 *
 * @author xuxiaowei
 * @see <a href="http://127.0.0.1:8080/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 code</a>
 * @see <a href="http://127.0.0.1:8080/oauth/token?code=qa8P6B&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&redirect_uri=http://127.0.0.1:123&grant_type=authorization_code">获取 Token</a>
 * @see <a href="http://127.0.0.1:8080/oauth/check_token?token=">检查 Token</a>
 * @see <a href="http://127.0.0.1:8080/oauth/token?client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&grant_type=refresh_token&refresh_token=">刷新 Token</a>
 * @since 0.0.1
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurerAdapterConfiguration extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public AuthorizationServerConfigurerAdapterConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        security.checkTokenAccess("permitAll()");
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 查询 Client
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * @see WhitelabelApprovalEndpoint
     * @see AuthorizationServerEndpointsConfiguration#authorizationEndpoint()
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 获取 Token 可使用 GET、POST
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // 刷新 Token 时查询用户
        endpoints.userDetailsService(jdbcDaoImpl());

        // code 持久化
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource) {
            private RandomValueStringGenerator generator = new RandomValueStringGenerator();

            /**
             * 重写 code 持久化，自定义 code 长度
             */
            @Override
            public String createAuthorizationCode(OAuth2Authentication authentication) {
                generator.setLength(32);
                String code = generator.generate();
                store(code, authentication);
                return code;
            }
        });

        // Token 持久化
        endpoints.tokenStore(new JdbcTokenStore(dataSource));

        // 自定义显示授权服务器的批准页面。
        endpoints.pathMapping("/oauth/confirm_access", "/oauth/customize_confirm_access");

        // 加密 Token
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("hgiYUt%^&%hiiuoHIH");
        endpoints.tokenEnhancer(jwtAccessTokenConverter);
    }

    /**
     * 刷新 Token 时查询用户
     */
    public JdbcDaoImpl jdbcDaoImpl() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        return jdbcDao;
    }

}
