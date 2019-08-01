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
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.support.SessionStatus;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 授权服务器配置
 * <p>
 * redirect_uri：
 * 当数据库中只用一个redirect_uri时，可缺省
 * 参见：
 * {@link AuthorizationEndpoint#authorize(Map, Map, SessionStatus, Principal)}
 * {@link DefaultRedirectResolver#resolveRedirect(String, ClientDetails)}
 * {@link DefaultRedirectResolver#obtainMatchingRedirect(Set, String)}
 * <p>
 * 关于 jti：
 * 使用{@link UUID#randomUUID()#toString()}创建jti
 * {@link DefaultTokenServices#createAccessToken(OAuth2Authentication)}
 * {@link DefaultTokenServices#createAccessToken(OAuth2Authentication, OAuth2RefreshToken)}
 * <p>
 * 关于 Token：
 * AccessToken：默认为 12小时 {@link DefaultTokenServices#accessTokenValiditySeconds}
 * refreshToken：默认为 30 天 {@link DefaultTokenServices#refreshTokenValiditySeconds}
 *
 * @author xuxiaowei
 * @see <a href="http://127.0.0.1:8080/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 code（静默授权）</a>
 * @see <a href="http://127.0.0.1:8080/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_userinfo&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 code</a>
 * @see <a href="http://127.0.0.1:8080/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=token&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">获取 Token（implicit，简化模式）</a>
 * @see AuthorizationEndpoint#authorize(Map, Map, SessionStatus, Principal)
 * @see <a href="http://127.0.0.1:8080/oauth/token?code=evP7tmtrRk7Y5bhewjRCo9iUMzcFyByi&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&redirect_uri=http://127.0.0.1:123&grant_type=authorization_code">获取 Token</a>
 * @see TokenEndpoint#getAccessToken(Principal, Map)
 * @see <a href="http://127.0.0.1:8080/oauth/check_token?token=">检查 Token</a>
 * @see CheckTokenEndpoint#checkToken(String)
 * @see <a href="http://127.0.0.1:8080/oauth/token?client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&grant_type=refresh_token&refresh_token=">刷新 Token</a>
 * @see TokenEndpoint#getAccessToken(Principal, Map)
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

    /**
     * permitAll授予对任何人的访问权限，hasRole（'ROLE_ADMIN'）要求用户具有角色'ROLE_ADMIN'。
     *
     * @see ExpressionUrlAuthorizationConfigurer.AuthorizedUrl#permitAll
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        security.checkTokenAccess("permitAll()");

        // 允许 Client 进行表单验证（URL），否则将出现弹窗输入 ClientId、ClientSecret
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 查询 Client
        clients.withClientDetails(new JdbcClientDetailsService(dataSource) {

            /**
             * 重写查询 Client 自定义异常
             * <p>
             * Client 不存在时的异常
             */
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
                try {
                    return super.loadClientByClientId(clientId);
                } catch (RuntimeException e) {
                    throw OAuth2Exception.create(OAuth2Exception.INVALID_CLIENT, "非法参数：client_id");
                }
            }
        });
    }

    /**
     * endpoints.tokenEnhancer(TokenEnhancer)：
     * 在{@link AuthorizationServerTokenServices}实现存储之前增强访问令牌的策略。
     *
     * @see WhitelabelApprovalEndpoint
     * @see WhitelabelErrorEndpoint
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
             * <p>
             * 默认长度为 6
             *
             * @see RandomValueAuthorizationCodeServices#createAuthorizationCode(OAuth2Authentication)
             * @see RandomValueStringGenerator#RandomValueStringGenerator()
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

        // 自定义 用于显示授权服务器的错误页面（响应）。
        endpoints.pathMapping("/oauth/error", "/oauth/customize_error");

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
