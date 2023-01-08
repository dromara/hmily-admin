# hmily-admin

Hmily-admin是柔性分布式事务中间件hmily的配套展示平台，**请搭配[hmily](https://github.com/dromara/hmily) 使用**。
若您想更多个性化的需求，请下载[hmily-dashboard](https://github.com/dromara/hmily-dashboard) 自行修改。

## 部署/本地启动

* [**使用 Docker 快速部署（推荐）**](quick-start.md)
* [通过 GitHub release 包部署](deploy-from-release.md)
* [从源码本地启动](./run-from-source.md)

## Hmily-admin 特性解读

* 根据不同的查询条件对全局事务进行检索
  应用名：一般为微服务的应用名称，需要保证唯一性。
  模式类型下拉框：当前版本仅支持TCC。
  全局事务状态： 包括运行中、成功、失败。
  时间范围：可以根据事务的开始和结束时间进行搜索。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/searchConditions.jpg)

* 查看全局事务信息
  一个分布式事务理解成一个包含来若干分支事务的全局事务，
  全局事务的职责是协调其下管辖的分支事务达成一致，
  要么一起成功提交，要么一起失败回滚。此外，通常分 支事务本身就是一个关系数据库的本地事务。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/TransInfo.jpg)

* 查看分支事务信息
  分支事务作为每一个系统中事务的实际参与者，会记录很多详细的信息。
  在这里，我们提供了分支事务的
  应用名、全局事务ID、分支事务ID、参与者角色、模式类型、分支事务状态、重试次数、版本、事务接口、事务方法、confirm方法、cancel方法、创建时间、最后执行时间
  等信息。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/participantInfo.jpg)

* 编辑重试次数（手动补偿）
  点击“重试”：事务用户可以在引入配置文件中设置事务的最大重试次数，如果事务异常，达到最大重试次数后，
  用户还想继续让事务进行一定次数的重试操作，就可以点击此“重试”按钮，进行重试次数的设置（但是不能超过最大重试次数）。
  注意，这里设置的重试次数是已经执行的重试次数，即还会执行 （最大重试次）- （编辑设置） 次重试.
  此方式主要用于您的系统突然发生崩溃后，重启项目来达成事务的一致性。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/retry.jpg)


* 获取补偿信息
  如果导致分支事务异常的原因是自身应用业务问题，就不能通过重试解决了。这时，我们可以通过查看补偿信息来进行手动补。
  注意，这里提供的补偿信息，是序列化后转化成的16进制string字符串，需要您复制到自己的应用里，利用hmily携带的反序列化方法进行反序列化解析。
  得到一份较为清晰的说明后，按照说明信息进行手动补偿 。请在补偿完成后，手动删除该条分支事务信息。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/compensation.jpg)


* 删除分支事务
  如果分支事务异常的原因由于环境问题导致的，并且问题已经解决，自动或者手动配置重试次数，
  分支事务可以正常，如果导致分支事务异常的原因是自身应用业务问题，就不能通过重试解决了。
  若用户手动处理的分支事务由于分布式事务平台内部并不感知事务的状态，所以需要用户手动点击对应分支事务的“删除”按钮进行清理，
  也可以批量进行删除，此删除操作需谨慎操作，以免由于误操作导致无法找到对应分支事务数据。
  ![](https://github.com/yu199195/yu199195.github.io/tree/master/images/hmily/delete.jpg)

## 欢迎交流沟通

如有任何问题欢迎加入QQ群：162614487 进行讨论