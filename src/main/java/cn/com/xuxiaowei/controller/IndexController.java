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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 主页/域名 Controller
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Controller
public class IndexController {

    /**
     * 主页/域名
     */
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpSession session = request.getSession();
        Object springSecurityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");

        if (springSecurityContext instanceof SecurityContextImpl) {
            SecurityContextImpl securityContext = (SecurityContextImpl) springSecurityContext;
            Authentication authentication = securityContext.getAuthentication();
            System.err.println(authentication);
        }

        System.err.println();

        // 或
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.err.println(authentication);

        return "index";
    }

}
