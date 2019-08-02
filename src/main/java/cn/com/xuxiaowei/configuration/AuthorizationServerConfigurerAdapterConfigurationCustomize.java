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

import cn.com.xuxiaowei.customize.EnableAuthorizationServerCustomize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.support.SessionStatus;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

/**
 * 授权服务器配置
 *
 * @author xuxiaowei
 * @see AuthorizationServerSecurityConfiguration
 * @see <a href="http://127.0.0.1:8080/oauth/authorize/customize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 code（静默授权）</a>
 * @see <a href="http://127.0.0.1:8080/oauth/authorize/customize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_userinfo&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 code</a>
 * @see <a href="http://127.0.0.1:8080/oauth/authorize/customize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=token&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 Token（implicit，简化模式）</a>
 * @see AuthorizationEndpoint#authorize(Map, Map, SessionStatus, Principal)
 * @see <a href="http://127.0.0.1:8080/oauth/token/customize?code=eu168h&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&redirect_uri=http://127.0.0.1:123&grant_type=authorization_code">获取 Token</a>
 * @see TokenEndpoint#getAccessToken(Principal, Map)
 * @see <a href="http://127.0.0.1:8080/oauth/check_token/customize?token=">检查 Token</a>
 * @see CheckTokenEndpoint#checkToken(String)
 * @see <a href="http://127.0.0.1:8080/oauth/token/customize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&grant_type=refresh_token&refresh_token=">刷新 Token</a>
 * @see TokenEndpoint#getAccessToken(Principal, Map)
 * @see EnableAuthorizationServerCustomize
 * @since 0.0.1
 */
@Configuration
@EnableAuthorizationServerCustomize
public class AuthorizationServerConfigurerAdapterConfigurationCustomize extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public AuthorizationServerConfigurerAdapterConfigurationCustomize(DataSource dataSource) {
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
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        endpoints.userDetailsService(jdbcDaoImpl());
        endpoints.authorizationCodeServices(new InMemoryAuthorizationCodeServices());
        endpoints.tokenStore(new InMemoryTokenStore());
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
