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
package cn.com.xuxiaowei.service.impl;

import cn.com.xuxiaowei.entity.QrCode;
import cn.com.xuxiaowei.service.QrCodeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 二维码扫描登录授权 Redis 服务 实现类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Service
public class QrCodeServiceImpl implements QrCodeService {

    private static final String VALUE = "QrCode";

    /**
     * 将 二维码扫描登录授权 信息放入 Redis
     * <p>
     * 无论如何都保存到 Redis
     *
     * @param entity
     * @return
     */
    @Override
    @CachePut(value = VALUE, key = "#entity.uuid")
    public QrCode saveCachePut(QrCode entity) {
        return entity;
    }

    /**
     * 根据 二维码扫描登录授权 UUID 查询 Redis
     * <p>
     * 仅查询 Redis
     * <p>
     * Redis 中存在时返回实体类，不存在时，返回 null
     * 可使用<code>return new QrCode();</code>，如果Redis中为 null，返回的是实体类，但所有值均为 null（实体类默认值）
     *
     * @param uuid
     * @return
     */
    @Override
    @Cacheable(value = VALUE, key = "#uuid")
    public QrCode getByUuidCacheable(String uuid) {
        return null;
    }

    /**
     * 根据 二维码扫描登录授权 UUID 删除 Redis
     *
     * @param uuid
     * @return
     */
    @Override
    @CacheEvict(value = VALUE, key = "#uuid")
    public void removeCacheEvict(String uuid) {

    }

}
