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

import cn.com.xuxiaowei.entity.QrCode;
import cn.com.xuxiaowei.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 二维码扫描授权登录 Controller
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Controller
//@FrameworkEndpoint
//@SessionAttributes({OauthAuthorizeQrCodeController.AUTHORIZATION_REQUEST_ATTR_NAME, OauthAuthorizeQrCodeController.ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME})
public class OauthAuthorizeQrCodeController extends AbstractEndpoint {

    static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";

    static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "cn.com.xuxiaowei.controller.OauthAuthorizeQrCodeController.ORIGINAL_AUTHORIZATION_REQUEST";

    private QrCodeService qrCodeService;

    @Autowired
    public void setQrCodeService(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * @see AuthorizationEndpoint#authorize(Map, Map, SessionStatus, Principal)
     */
    @RequestMapping(value = "/oauth/authorize/qr")
    public String confirm(HttpServletRequest request, HttpServletResponse response, Model model,
                          @RequestParam Map<String, String> parameters) {

        AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(parameters);

        String uuid = UUID.randomUUID().toString();

        LocalDateTime createTime = LocalDateTime.now();
        QrCode qrCode = new QrCode(uuid, createTime, model);
        qrCodeService.saveCachePut(qrCode);

        model.addAttribute("uuid", uuid);

        return "oauth/customize_confirm_access_qr";
    }

}
