# Hmily-admin QuickStart(部署手册)
Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，请搭配[hmily](https://github.com/dromara/hmily) 使用。
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

## hmily-admin部署启动教程(linux)

**启动前提：** 
  用户必须使用的=JDK必须是`1.8+` ，并且已在服务器上安装好，有且仅有一套`1.8+`
  
### 步骤一：解压文件

  * 先将提供的zip包上传后，自定义位置进行解压操作:
  
   ```
    unzip hmily-admin-1.0.1.zip 

   ```
### 步骤二：修改配置文件
  * 方法一：进入解压后的包之后，修改相对应的配置文件，并执行启动脚本:
   ```
      sh bin/start.sh 
   
   ```
  * 方法二： 提供了一些便捷的方式可以避免手动修改配置文件:
   ```
      #若只是更换数据库类型
      sh bin/start.sh -mongo
      
      #若想更换数据库类型及其其他信息，可使用
      sh bin/start.sh -mysql -url127.0.0.1:3306/hmily -uroot -p123456
      
   ```
### 步骤三：查看日志
   * 查看日志:
   ```
      tail -100f /export/log/hmily-admin/console.log 

   ```
   
## hmily-admin部署启动教程(windows)

### 步骤一：解压文件
* 将提供的zip包上传后，手动解压。
### 步骤二：修改配置文件
* 进入解压后文件的config目录，手动修改配置文件。
### 步骤三：启动项目
* 进入解压后文件的bin目录，双击start.bat。
### 步骤四：查看日志
* 日志将记录在解压后文件的console.log里。

### 如有任何问题欢迎加入QQ群：162614487 进行讨论
