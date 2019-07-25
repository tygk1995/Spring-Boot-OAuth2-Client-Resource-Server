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

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * 确认访问授权 Controller
 * <p>
 * 用于显示授权服务器的批准页面。
 *
 * @author xuxiaowei
 * @see SessionAttributes
 */
@Controller
@SessionAttributes("authorizationRequest")
public class ConfirmAccessController {

    /**
     * 确认访问授权 页面
     *
     * @see WhitelabelApprovalEndpoint
     * @see SessionAttributes
     */
    @RequestMapping("/oauth/customize_confirm_access")
    public String customizeConfirmAccess(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> map = model.asMap();
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) map.get("authorizationRequest");
        Set<String> scopes = authorizationRequest.getScope();
        String clientId = authorizationRequest.getClientId();

        Object csrf = request.getAttribute("_csrf");
        // csrf 未关闭
        if (csrf instanceof CsrfToken) {
            CsrfToken csrfToken = (CsrfToken) csrf;
            model.addAttribute("_csrf", csrfToken.getToken());
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("scopes", scopes);
        return "oauth/customize_confirm_access";
    }

}
