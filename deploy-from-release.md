# Hmily-admin QuickStart(部署手册)

Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，**请搭配[hmily](https://github.com/dromara/hmily) 使用。**
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

本文将使用 GitHub release 文件部署 hmily-admin

## 1. 先决条件

* 分布式事务项目[hmily](https://github.com/dromara/hmily)已经部署并且运行起来，用户根据自己的RPC框架进行使用。
* 数据库：[MySQL](https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html) or [MongoDB](https://www.mongodb.com/docs/manual/installation/)
* 用户使用的JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`。
* 装有maven。

## 2. 部署流程

### 2.1 获取release包文件并解压

获取地址见：[hmily-admin-${version}-admin-bin.tar.gz](https://github.com/dromara/hmily-admin/releases/)，如：

```bash
wget https://github.com/dromara/hmily-admin/releases/download/1.0.2/hmily-admin-1.0.2-admin-bin.tar.gz
```

将下载的` hmily-admin-${current.version}-admin-bin.tar.gz`文件，移到自定义位置，进行解压操作:

```bash
tar -zxvf  hmily-admin-${current.version}-admin-bin.tar.gz
```
  
### 2.2 修改配置文件并启动

一共有两种方法：

#### 2.2.1 方法一

进入解压后的包之后，**修改相对应的配置文件**（在`hmily-admin-${current.version}-admin-bin/conf`文件夹中），具体可参考[readme](https://github.com/dromara/hmily-admin/blob/master/README.md)。再执行启动脚本:

```bash
cd hmily-admin-${current.version}-admin-bin/
# 修改hmily-admin-${current.version}-admin-bin/conf中的配置文件后，再执行start.sh
bash -x bin/start.sh
```

**若遇到权限问题**，导致启动时一直循环`sleep 1`，可将`bash -x bin/start.sh`命令换成`sudo bash -x bin/start.sh`。启动成功后，会显示进程号。

#### 2.2.2 方法二

 提供了一些便捷的方式可以避免手动修改配置文件:

```bash
#若只是更换数据库类型
bash -x bin/start.sh -mongo

#若想更换数据库类型及其其他信息，可使用
bash -x bin/start.sh -mysql -url127.0.0.1:3306/hmily -uroot -p123456
```

### 3. 访问

浏览器打开 http://localhost:8888 以访问。

账户 `admin`，密码 `admin`。

### 4. 查看日志
  
```bash
    tail -100f /export/log/hmily-admin/console.log
```
  
### 如有任何问题欢迎加入QQ群：162614487 进行讨论
