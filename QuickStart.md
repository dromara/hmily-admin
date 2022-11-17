
# Hmily-admin QuickStart(部署手册)
Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，**请搭配[hmily](https://github.com/dromara/hmily) 使用。**
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

hmily-admin含有**两种部署方式**：`release`方式、`docker`方式，您可根据自己的需要进行选择。

## 一、docker方式

**启动前提**：

- 用户使用的JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`

- 装有maven
- 安装了`make`命令
- 安装了docker

#### 步骤一：拉取镜像

```bash
      docker pull dromara/hmily-admin:${current.version}
```

执行成功后，可以通过`docker images`查看生成的镜像。

#### 步骤二：运行容器

这里以使用mysql为例进行讲解，使用mongo时同理即可。

**第一种方式：**

- 直接命令行传入参数：

  ```bash
        docker run -p 8888:8888 -d --name ${your_container_name} \
        -e "SPRING_PROFILES_ACTIVE=mysql" \
        -e  "hmily.admin.hmilyDbConfig.url=jdbc:mysql://${your_ip_port}/hmily?useUnicode=true&characterEncoding=utf8" \
        -e  "hmily.admin.hmilyDbConfig.username=${your_username}" \
        -e  "hmily.admin.hmilyDbConfig.password=${your_password}" \
        dromara/hmily-admin:${current.version}
  ```

- `-e`后的参数名可参考`hmily-admin-service/src/main/resources`里具体配置文件中的信息。

**第二种方式**：

- 首先将 `hmily-admin-service/src/main/resources`下的`application.yml`、`application-mysql.yml`、`application-mongo.yml`复制到`${your_work_dir}/conf`， 调整`application.yml`中的配置`spring.profiles.active = mysql`，再修改`application-mysql.yml`文件中的配置信息为你自己的。

- 再执行以下语句：

  ```bash
        docker run -v ${your_work_dir}/conf:/opt/hmily-admin/conf \
        -v ${your_work_dir}/logs:/opt/hmily-admin/logs \
        -d -p 8888:8888 --name ${your_container_name} \ 
        dromara/hmily-admin:${current.version}
  ```
  
####  步骤三：查看日志

* 查看日志:

  ```bash
     tail -100f ${your_work_dir}/logs/console.log
  ```

## 二、release方式

### linux下的部署

**启动前提：**

- 用户使用的JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`。

- 装有maven。

#### 步骤一：获取release包文件

获取地址见：[hmily-admin-${version}-admin-bin.tar.gz](https://github.com/dromara/hmily-admin/releases/)，如：

```bash
      wget https://github.com/dromara/hmily-admin/releases/download/1.0.2/hmily-admin-1.0.2-admin-bin.tar.gz
```

#### 步骤二：解压文件

* 将下载的` hmily-admin-${current.version}-admin-bin.tar.gz`文件，移到自定义位置，进行解压操作:

   ```bash
      tar -zxvf  hmily-admin-${current.version}-admin-bin.tar.gz
   ```
  
#### 步骤三：修改配置文件并启动

* **方法一**：进入解压后的包之后，**修改相对应的配置文件**（在`hmily-admin-${current.version}-admin-bin/conf`文件夹中），具体可参考[readme](https://github.com/dromara/hmily-admin/blob/master/README.md)。再执行启动脚本:

   ```bash
      cd hmily-admin-${current.version}-admin-bin/
      # 修改hmily-admin-${current.version}-admin-bin/conf中的配置文件后，再执行start.sh
      bash -x bin/start.sh
   ```
     **若遇到权限问题**，导致启动时一直循环`sleep 1`，可将`bash -x bin/start.sh`命令换成`sudo bash -x bin/start.sh`。启动成功后，会显示进程号。

* **方法二**： 提供了一些便捷的方式可以避免手动修改配置文件:

   ```bash
      #若只是更换数据库类型
      bash -x bin/start.sh -mongo
      
      #若想更换数据库类型及其其他信息，可使用
      bash -x bin/start.sh -mysql -url127.0.0.1:3306/hmily -uroot -p123456
   ```
#### 步骤四：查看日志
* 查看日志:
   ```bash
      tail -100f /export/log/hmily-admin/console.log
   ```
  
### 如有任何问题欢迎加入QQ群：162614487 进行讨论
