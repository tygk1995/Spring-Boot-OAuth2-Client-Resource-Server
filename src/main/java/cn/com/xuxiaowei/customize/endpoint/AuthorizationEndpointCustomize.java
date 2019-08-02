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

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.security.Principal;
import java.util.Map;

/**
 * 授权 自定义
 * <p>
 * 主要为自定义 URL（不自定义时 Bean 冲突，出错）
 *
 * @author xuxiaowei
 * @see AuthorizationEndpoint
 * @since 0.0.1
 */
public class AuthorizationEndpointCustomize extends AuthorizationEndpoint {

    @Override
    @RequestMapping(value = "/oauth/authorize/customize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
                                  SessionStatus sessionStatus, Principal principal) {
        return super.authorize(model, parameters, sessionStatus, principal);
    }

    @Override
    @RequestMapping(value = "/oauth/authorize/customize", method = RequestMethod.POST, params = OAuth2Utils.USER_OAUTH_APPROVAL)
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
                              SessionStatus sessionStatus, Principal principal) {
        return super.approveOrDeny(approvalParameters, model, sessionStatus, principal);
    }

}
