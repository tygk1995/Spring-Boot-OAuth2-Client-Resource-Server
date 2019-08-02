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
package cn.com.xuxiaowei.customize.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

/**
 * 验证 Token 自定义
 * <p>
 * 主要为自定义 URL（不自定义时 Bean 冲突，出错）
 *
 * @author xuxiaowei
 * @see TokenEndpoint
 * @since 0.0.1
 */
public class TokenEndpointCustomize extends TokenEndpoint {

    @Override
    @RequestMapping(value = "/oauth/token/customize", method = RequestMethod.GET)
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return super.getAccessToken(principal, parameters);
    }

    @Override
    @RequestMapping(value = "/oauth/token/customize", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return super.postAccessToken(principal, parameters);
    }

}
