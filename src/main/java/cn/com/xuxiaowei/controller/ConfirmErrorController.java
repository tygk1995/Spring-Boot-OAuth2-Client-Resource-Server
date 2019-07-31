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

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 用于显示授权服务器的错误页面（响应）。
 *
 * @author xuxiaowei
 * @see WhitelabelErrorEndpoint
 * @since 0.0.1
 */
@Controller
public class ConfirmErrorController {

    private static final String ERROR = "<html><body><h1>OAuth 错误</h1><p>%errorSummary%</p></body></html>";

    /**
     * 自定义 用于显示授权服务器的错误页面（响应）。
     * <p>
     * errcode：401
     * error：invalid_client（客户凭证无效）
     * errmsg：Bad client credentials（客户端凭据错误）
     * 可能出现的原因：1、数据库不存在 client_id
     * <p>
     * errcode：400
     * error：invalid_request（非法请求）
     * errmsg：At least one redirect_uri must be registered with the client.（必须至少向客户注册一个redirect_uri。）
     * 可能出现的原因：1、数据库中 redirect_uri 为 null
     * errmsg：Invalid redirect: http://127.0.0.1:123 does not match one of the registered values.（无效的重定向：http：//127.0.0.1：123与其中一个注册值不匹配。）
     * <p>
     * errcode：400
     * error：invalid_request（非法请求）
     * errmsg：Invalid redirect: http://127.0.0.1:123 does not match one of the registered values.（无效的重定向：http：//127.0.0.1：123与其中一个注册值不匹配。）
     * 可能出现的原因：1、数据库不存在 Client 发送的 redirect_uri
     * <p>
     * errcode：400
     * error：unsupported_response_type（不支持的响应类型）
     * errmsg：Unsupported response types: [code]
     *
     * @see WhitelabelErrorEndpoint
     * @see SessionAttributes
     */
    @RequestMapping("/oauth/customize_error")
    public ModelAndView customizeConfirmAccess(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> model = new HashMap<>(4);
        Object error = request.getAttribute("error");

        // 错误摘要可能包含恶意用户输入，
        // 它需要被转义以防止XSS
        String errorSummary;
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            errorSummary = HtmlUtils.htmlEscape(oauthError.getSummary());
        } else {
            errorSummary = "Unknown error";
        }
        String errorContent = ERROR.replace("%errorSummary%", errorSummary);
        View errorView = new View() {
            @Override
            public String getContentType() {
                return "text/html;charset=UTF-8";
            }

            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType(getContentType());
                response.getWriter().append(errorContent);
            }
        };
        return new ModelAndView(errorView, model);
    }

}
