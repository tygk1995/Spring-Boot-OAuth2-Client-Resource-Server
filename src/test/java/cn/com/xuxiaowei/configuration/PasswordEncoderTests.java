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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder 密码编辑器 测试类
 * <p>
 * 说明：
 * 使用{@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}加密数据，不同于MD5，
 * 针对相同数据，每次加密结果都不相同
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
class PasswordEncoderTests {

    /**
     * 创建密码：
     * 未加密密码：123
     * 加密密码：{bcrypt}$2a$10$nDGmklGtTcL/AWNisIqgJ.p8z0teas89FhMAGdVSNlQxR/uMG/ZrS
     */
    @Test
    public void createPassword() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encode = delegatingPasswordEncoder.encode("123");
        log.info(encode);
    }

    /**
     * 密码比较
     */
    @Test
    public void matches() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        boolean matches = delegatingPasswordEncoder.matches("123", "{bcrypt}$2a$10$nDGmklGtTcL/AWNisIqgJ.p8z0teas89FhMAGdVSNlQxR/uMG/ZrS");
        log.info(String.valueOf(matches));
    }

    /**
     * 创建 ClientSecret：
     * 未加密 ClientSecret：da4ce585e30346d3a876340d49e25a01
     * 加密 ClientSecret：{bcrypt}$2a$10$/f6qc5liQvYuZMUZlec3aOcZqd.TKxtmOtmVTJzyxVupoK31zGCW.
     */
    @Test
    public void createClientSecret() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encode = delegatingPasswordEncoder.encode("da4ce585e30346d3a876340d49e25a01");
        log.info(encode);
    }

}
