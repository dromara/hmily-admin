# Hmily-admin QuickStart

Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，**请搭配[hmily](https://github.com/dromara/hmily) 使用。**
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

本文将使用 Docker 快速部署 hmily-admin

## 先决条件

* 分布式事务项目[hmily](https://github.com/dromara/hmily)已经部署并且运行起来，用户根据自己的RPC框架进行使用。
* [Docker](https://docs.docker.com/get-started/overview/)
* 数据库：[MySQL](https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html) or [MongoDB](https://www.mongodb.com/docs/manual/installation/)

## 1. 拉取镜像

```bash
docker pull dromara/hmily-admin
```

执行成功后，可以通过`docker images`查看生成的镜像。

## 2. 运行容器

这里以使用 MySQL 为例进行讲解，MongoDB 同理。

有两种方式，一种是用命令行传入数据库信息，一种是直接修改 yaml 文件然后挂载至 Docker 中。选择一种即可。

### 2.1 法一：命令行传参

先配置一下参数，请按真实情况修改`=`右侧的内容：

```bash
export mysql_ip_port="127.0.0.1:3306"
export mysql_username="root"
export mysql_password="123456"
```

然后我们就能直接运行下面这个命令了（直接复制，不需要修改）：

```bash
docker run -p 8888:8888 -d --name hmily-admin \
-e "SPRING_PROFILES_ACTIVE=mysql" \
-e  "hmily.admin.hmilyDbConfig.url=jdbc:mysql://${mysql_ip_port}/hmily?useUnicode=true&characterEncoding=utf8" \
-e  "hmily.admin.hmilyDbConfig.username=${mysql_username}" \
-e  "hmily.admin.hmilyDbConfig.password=${mysql_password}" \
dromara/hmily-admin
```

其中，如果数据库是 MongoDB，可以参考 [`hmily-admin-service/src/main/resources`](https://github.com/dromara/hmily-admin/blob/master/hmily-admin-service/src/main/resources/application-mysql.yml) 配置文件修改一下对应的参数。

### 2.2 法二：直接修改 yaml 文件

先选定/创建一个本机目录用来存放配置文件，需要是绝对路径，假设是 `/root/hmily-admin`（请确保当前用户有该目录写入权限）。

把这个命令设置成变量，方便后面直接复制本文档的命令，右侧的内容可以按实际情况修改：

```bash
export work_dir="/root/hmily-admin"
```

下载对应的配置文件到工作目录（直接复制）：

```bash
# 创建 conf 文件夹
mkdir -p ${work_dir}/conf
# 下载 application.yml
curl -o ${work_dir}/conf/application.yml https://raw.githubusercontent.com/dromara/hmily-admin/master/hmily-admin-service/src/main/resources/application.yml
# 下载数据库配置，application-mysql.yml（mongodb类似，请自行更改文件地址）
curl -o ${work_dir}/conf/application-mysql.yml https://raw.githubusercontent.com/dromara/hmily-admin/master/hmily-admin-service/src/main/resources/application-mysql.yml
# 进入工作目录下的conf文件夹
cd ${work_dir}/conf
```

确认 `application.yml` 中的配置 `spring.profiles.active` 为 `mysql`，再修改 `application-mysql.yml` 文件中的数据库连接信息。

最后，执行以下命令（直接复制）：

```bash
docker run -v ${work_dir}/conf:/opt/hmily-admin/conf \
-d -p 8888:8888 --name hmily-admin \
dromara/hmily-admin
```

## 3. 访问

浏览器打开 http://localhost:8888 以访问。

账户 `admin`，密码 `admin`。
  
## 4. 查看日志

```bash
    docker logs hmily-admin
```

