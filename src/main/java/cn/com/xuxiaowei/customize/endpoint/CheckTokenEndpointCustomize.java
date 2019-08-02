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

import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 检查 Token 自定义
 * <p>
 * 主要为自定义 URL（不自定义时 Bean 冲突，出错）
 *
 * @author xuxiaowei
 * @see CheckTokenEndpoint
 * @since 0.0.1
 */
public class CheckTokenEndpointCustomize extends CheckTokenEndpoint {

    public CheckTokenEndpointCustomize(ResourceServerTokenServices resourceServerTokenServices) {
        super(resourceServerTokenServices);
    }

    @Override
    @RequestMapping(value = "/oauth/check_token/customize")
    public Map<String, ?> checkToken(@RequestParam("token") String value) {
        return super.checkToken(value);
    }

}
