# 从源码启动 hmily-admin

**启动前提：** 分布式事务项目[hmily](https://github.com/dromara/hmily)已经部署并且运行起来，用户根据自己的RPC框架进行使用。

hmily-admin分为两个模块：hmily-admin-service、hmily-admin-dist。

- hmily-admin-service：项目的主体服务，包含项目的所有功能，可单独启动。同时包含`release`部署方案，详细部署方法见：[快速部署体验](https://github.com/dromara/hmily-admin/blob/master/QuickStart.md)
- hmily-admin-dist：打包模块，用于docker部署。详细部署方法见：[快速部署体验](https://github.com/dromara/hmily-admin/blob/master/QuickStart.md)

>注： **以下启动教程只涉及`hmily-admin-service`模块。**

## 步骤零：准备工作

* 首先用户使用的JDK必须是`1.8+` ，本地安装了`git`、`maven` ，执行以下命令以下载项目：

```
git clone https://github.com/dromara/hmily-admin.git
```

* 使用你的开发工具打开项目，比如idea、Eclipse

## 步骤一：更改配置

* 在`hmily-admin-service`模块的`application.yml`文件中，修改您的服务端口、日志存储（数据库）类型、账户信息等配置:

   ```yml
      #admin项目的tomcat端口
      server:
          port: 8888
          servlet:
            context-path:     # 项目访问的url前缀，可不配置

      #profiles.active 在hmily中指定的日志存储源类型
       spring:
          application:
            name: hmily-admin
          profiles:
            active: mysql
     
      #登录admin项目的账户密码
       hmily:
         admin :
           userName : admin
           password : admin
   ```
![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/config.jpg)

* 根据在上述`application.yml`中配置的`spring.profiles.active`类型，更改对应的`application-mongo.yml`或`application-mysql.yml`文件。
* **当前版本的admin仅支持`mysql`和`mongo`等两种存储方式**。
  `application-mongo.yml`参考如下：

   ```
   hmily:
      admin :
         retryMax : 10
         repository : mongo
         hmilyMongoConfig :
               mongoDbUrl  : 127.0.0.1:27017
               mongoDbName  :  hmily
               mongoUserName  : root
               mongoUserPwd   : 1
   ```
![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/mongoConfig.jpg)

`application-mysql.yml` 参考如下：
   ```
   hmily:
      admin :
         retryMax : 10
         repository : mysql
         hmilyDbConfig :
               driverClassName:  com.mysql.jdbc.Driver
               url: jdbc:mysql://localhost:3306/hmily?useUnicode=true&amp;characterEncoding=utf8
               username: root
               password: 1
   ```

![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/mysqlConfig.jpg)

## 步骤二：运行 AdminApplication 中的main方法。

## 步骤三：访问Hmily-admin
* 在浏览器访问 http://ip:port （默认是localhost:8888) ，账户 `admin`，密码 `admin`。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/login.jpg)