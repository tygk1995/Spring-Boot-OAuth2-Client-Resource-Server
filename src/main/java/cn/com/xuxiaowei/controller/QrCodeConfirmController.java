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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码扫描登录授权 Controller
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Controller
@SessionAttributes("authorizationRequest")
public class QrCodeConfirmController {

    private QrCodeService qrCodeService;

    @Autowired
    public void setQrCodeService(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     *
     */
    @RequestMapping(value = "/oauth/authorize/qr/confirm", params = "uuid")
    public String confirm(HttpServletRequest request, HttpServletResponse response, Model model,
                          String uuid) {

        QrCode byUuidCacheable = qrCodeService.getByUuidCacheable(uuid);

        System.err.println(byUuidCacheable);

        return "/oauth/oauth_authorize_confirm";
    }

    /**
     *
     */
    @RequestMapping(value = "/oauth/authorize/qr/confirm")
    @ResponseBody
    public Map<String, Object> confirm(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", 403);
        map.put("msg", "非法 Client");
        return map;
    }

}
