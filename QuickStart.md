# Hmily-admin QuickStart(部署手册)
Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，**请搭配[hmily](https://github.com/dromara/hmily) 使用。**
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

hmily-admin含有**两种部署方式**：`release`方式、`docker`方式，您可根据自己的需要进行选择。

## 一、release方式

### linux下的部署

**启动前提：**

- 用户使用的JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`。

- 装有maven。

#### 步骤一：maven打包

- 在项目目录下，使用maven进行打包，生成target目录。

```bash
    cd hmily-admin-service/
    mvn package
```

- 进入target目录，会发现生成了` hmily-admin-1.0.2.zip`文件。

#### 步骤二：解压文件

* 将生成的` hmily-admin-1.0.2.zip`文件，移到自定义位置，进行解压操作:

   ```bash
    mv target/hmily-admin-1.0.2.zip 自定义文件夹路径名/hmily-admin-1.0.2.zip
    cd 自定义文件夹路径名/
    unzip hmily-admin-1.0.2.zip 

   ```
#### 步骤三：修改配置文件
* 方法一：进入解压后的包之后，修改相对应的配置文件（在`hmily-admin-1.0.2/config`文件夹中），具体可参考[readme](https://github.com/dromara/hmily-admin/blob/master/README.md)。再执行启动脚本:
   ```bash
      cd hmily-admin-1.0.2/
      # 修改hmily-admin-1.0.2/config中的配置文件后，再执行start.sh
      bash -x bin/start.sh
    
   ```
​    **若遇到权限问题**，导致启动时一直循环`sleep 1`，可将`bash -x bin/start.sh`命令换成`sudo bash -x bin/start.sh`。启动成功后，会显示进程号。

* 方法二： 提供了一些便捷的方式可以避免手动修改配置文件:

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



## 二、docker方式

**启动前提**：

- 用户使用的JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`

- 装有maven
- 安装了`make`命令
- 安装了docker

#### 步骤一：生成docker镜像

进入项目目录，执行`make default`命令：

```bash
      cd 项目路径名/hmily-admin
      make default
```

执行成功后，可以通过`docker ps`查看生成的镜像。

#### 步骤二：运行容器

这里以使用mysql为例进行讲解，使用mongo时同理即可。

- 首先将 `hmily-admin-service/src/main/resources`下的`application.yml`、`application-mysql.yml`、`application-mongo.yml`复制到`${your_work_dir}/conf`， 调整`application.yml`中的配置`spring.profiles.active = mysql`，再修改`application-mysql.yml`文件中的配置信息。

- 执行以下语句：

  ```bash
        docker run -v ${your_work_dir}/conf:/opt/hmily-admin/conf \
        -d -p 8888:8888 dromara/hmily-admin:${current.version}
  ```



### 如有任何问题欢迎加入QQ群：162614487 进行讨论