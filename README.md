# spring-boot-oauth2-client-resource-server
 Spring Boot OAuth2 Client Resource Server

## 项目说明

- Spring Security
    - 用户登录
    
- Spring OAuth2 Client
    - 授权管理服务
    
- Spring OAuth2 Resource Server
    - 资源管理服务
    
## 使用说明

### 用户登录
- http://127.0.0.1:8080/login
- 用户名：xuxiaowei
- 密码：123
- 密码已加密：
    - [PasswordEncoder 密码编辑器 测试类](https://github.com/xuxiaowei-com-cn/Spring-Boot-OAuth2-Client-Resource-Server/blob/master/src/test/java/cn/com/xuxiaowei/configuration/PasswordEncoderTests.java)
    - [Spring Security 配置](https://github.com/xuxiaowei-com-cn/Spring-Boot-OAuth2-Client-Resource-Server/blob/master/src/main/java/cn/com/xuxiaowei/configuration/WebSecurityConfigurerAdapterConfiguration.java)
- SQL
    - users 表
        - enabled 是否启用用户
    - authorities 表
        - authority 用户拥有的角色
~~~
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `users`(`username`, `password`, `enabled`) VALUES ('xuxiaowei', '{bcrypt}$2a$10$nDGmklGtTcL/AWNisIqgJ.p8z0teas89FhMAGdVSNlQxR/uMG/ZrS', 1);
~~~
~~~
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `authority` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `authorities`(`username`, `authority`) VALUES ('xuxiaowei', 'ROLE_USER');
~~~
### 用户授权

- 获取 code
    - [获取 code URL](http://127.0.0.1:8080/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7)
        - client_id
            - 5e03fb292edd4e478cd7b4d6fc21518c
        - redirect_uri
            - http://127.0.0.1:123
        - response_type
            - code
        - scope
            - base
        - state
            - beff3dfc-bad8-40db-b25f-e5459e3d6ad7

- 获取 Token
    - [获取 Token URL](http://127.0.0.1:8080/oauth/token?code=&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&redirect_uri=http://127.0.0.1:123&grant_type=authorization_code)
        - client_id
            - 5e03fb292edd4e478cd7b4d6fc21518c
        - client_secret
            - da4ce585e30346d3a876340d49e25a01
        - redirect_uri
            - http://127.0.0.1:123
        - grant_type
            - authorization_code
        - code
            - 上一步获取的 code
    
- 检查 Token
    - [检查 Token URL](http://127.0.0.1:8080/oauth/check_token?token=)
        - token
            - 上一步的 access_token

- 刷新 Token
    - [刷新 Token URL](http://127.0.0.1:8080/oauth/token?client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&grant_type=refresh_token&refresh_token=)
        - client_id
            - 5e03fb292edd4e478cd7b4d6fc21518c
        - client_secret
            - da4ce585e30346d3a876340d49e25a01
        - grant_type
            - refresh_token
        - refresh_token
            - 获取 Token 中的 refresh_token

- SQL
    - oauth_client_details 表
        - Client 信息表
    - oauth_code 表
        - code 表
        - code 使用后删除数据
    - oauth_access_token 表
        - 储存 Token 表
        - 储存授权信息
    - oauth_refresh_token 表
        - 刷新 Token 表

~~~
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '未找到时：\r\nerror=\"invalid_client\", error_description=\"Bad client credentials\"',
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `resource_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '为null时：{\"error\":\"invalid_scope\",\"error_description\":\"Empty scope (either the client or the user is not allowed the requested scopes)\"}\r\n未匹配到时：\r\nerror=invalid_scope&error_description=Invalid%20scope:%20base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7&scope=userinfo',
  `authorized_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '为null时：\r\nerror=\"invalid_request\", error_description=\"At least one redirect_uri must be registered with the client.\"',
  `authorities` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token_validity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token_validity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `additional_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `autoapprove` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `oauth_client_details`(`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('5e03fb292edd4e478cd7b4d6fc21518c', '{bcrypt}$2a$10$/f6qc5liQvYuZMUZlec3aOcZqd.TKxtmOtmVTJzyxVupoK31zGCW.', NULL, 'base,userinfo', NULL, 'http://127.0.0.1:123', NULL, NULL, NULL, NULL, NULL);
~~~
~~~
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `authentication` blob
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
~~~
~~~
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`  (
  `token_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `token` blob,
  `authentication_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
~~~
~~~
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`  (
  `token_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `token` blob,
  `authentication` blob,
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
~~~
- 注意
    - client_secret 默认加密为 PasswordEncoderFactories.createDelegatingPasswordEncoder()

## 依赖说明

- Spring Web Starter（必须）
    - 使用Spring MVC构建Web，包括RESTful应用程序。 使用Apache Tomcat作为默认嵌入式容器。
        
- spring-security-oauth2-autoconfigure（必须）
    - version 2.1.6.RELEASE
    - 提供 Spring OAuth2 所需依赖
    
- Thymeleaf（非必须）
    - 适用于Web和独立环境的现代服务器端Java模板引擎。 允许HTML在浏览器中正确显示，并作为静态原型。
    
- MySQL Driver（非必须）
    - MySQL JDBC驱动程序。
    
- JDBC API（非必须）
    - 数据库连接API，用于定义客户端如何连接和查询数据库。
    - 用于用户登录时查询数据库，可自定义
    - 为授权服务（Spring OAuth2 Client）提供依赖
        - 查询 Client，可自定义
        - code 持久化，可自定义
        - Token 持久化，可自定义
        - Client 刷新Token时查询用户信息，可自定义
    
- thymeleaf-extras-springsecurity5（非必须）
    - Thymeleaf 页面权限控制
    