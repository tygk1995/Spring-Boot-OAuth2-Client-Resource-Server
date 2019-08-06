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

import cn.com.xuxiaowei.entity.QrCode;
import cn.com.xuxiaowei.service.QrCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Redis 测试类
 * <p>
 * 未配置（本项目不需要）Redis Session
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCacheCustomizeConfigurationTests {

    @Autowired
    private QrCodeService qrCodeService;

    /**
     * 保存到 Redis
     */
    @Test
    public void saveCachePut() {

        String uuid = UUID.randomUUID().toString();
        LocalDateTime createTime = LocalDateTime.now();

        QrCode qrCode = new QrCode();
        qrCode.setUuid(uuid);
        qrCode.setCreateTime(createTime);

        qrCodeService.saveCachePut(qrCode);
    }

    /**
     * 查询 Redis
     */
    @Test
    public void getByUuidCacheable() {
        QrCode byUuid = qrCodeService.getByUuidCacheable("c2275d9d-00ab-4739-976d-2ee4e05bd38b");
        System.err.println(byUuid);
    }

    /**
     * 删除 Redis
     */
    @Test
    public void removeCacheEvict() {
        qrCodeService.removeCacheEvict("5315398d-dc11-4528-9c7e-f5c4724129b4");
    }

}
