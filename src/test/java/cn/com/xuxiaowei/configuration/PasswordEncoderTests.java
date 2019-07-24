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

import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class PasswordEncoderTests {

    /**
     * {bcrypt}$2a$10$nDGmklGtTcL/AWNisIqgJ.p8z0teas89FhMAGdVSNlQxR/uMG/ZrS
     */
    @Test
    public void createPassword() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String encode = delegatingPasswordEncoder.encode("123");

        System.err.println(encode);
    }

    /**
     * {bcrypt}$2a$10$/f6qc5liQvYuZMUZlec3aOcZqd.TKxtmOtmVTJzyxVupoK31zGCW.
     */
    @Test
    public void createClientSecret() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String encode = delegatingPasswordEncoder.encode("da4ce585e30346d3a876340d49e25a01");

        System.err.println(encode);
    }

}
