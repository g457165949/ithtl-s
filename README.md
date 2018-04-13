# ithtl-s
后台权限管理系统
本项目是完全基于SpringBoot 1.5、Shiro、Layui、Vue 前后端分离项目，前端使用Nginx服务，Ajax接口/api进行接口转发服务端。

# 目的
学习并使用JAVA框架SpringBoot来开发。（QQ:457165949）

# 推荐博客
* [Spring boot 基础搭建](http://www.ithtl.com/?p=766) 
* [纯洁的微笑](http://www.ityouknow.com/spring-boot.html)
* [简书Springboot专题](https://www.jianshu.com/c/f0cf6eae1754)
* [方志朋Spring Boot 专栏](https://blog.csdn.net/column/details/15397.html)

# Nginx配置
        #添加头部信息
        proxy_set_header Cookie $http_cookie;
        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        
        #添加拦截路径和代理地址
        location /api/ {              
               proxy_pass http://localhost:8080/;  #注意：使用代理地址时末尾记得加上斜杠"/"。      
        }


# 页面展示

登录页面
 ![image](https://github.com/g457165949/ithtl-s/blob/master/doc/登录.jpeg)

用户页面
![image](https://github.com/g457165949/ithtl-s/blob/master/doc/用户.png)

权限页面
 ![image](https://github.com/g457165949/ithtl-s/blob/master/doc/菜单管理.png)
 
角色页面
![image](https://github.com/g457165949/ithtl-s/blob/master/doc/角色管理.png)

日志页面
![image](https://github.com/g457165949/ithtl-s/blob/master/doc/操作日志.png)