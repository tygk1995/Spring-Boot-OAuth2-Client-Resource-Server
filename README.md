# spring-boot-oauth2-client-resource-server
 Spring Boot OAuth2 Client Resource Server

## 项目说明

- Spring Security
    - 用户登录
    
- Spring OAuth2 Client
    - 授权管理服务
    
- Spring OAuth2 Resource Server
    - 资源管理服务
    
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
    