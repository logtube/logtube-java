# 升级日志

## 关于版本号的解释

版本号之后会稳定为 `X.Y.Z` 的格式

RC (Release Candidate, 发布候选) 代表尚在测试中，但是功能已经验证稳定

* `X` 为主版本号，目前固定为 `0`，因为还没有实现严格的 API 向后兼容
* `Y` 为次版本号，次版本号的变更代表新功能的引入
* `Z` 为问题修复版本号，**尽量选择相同主次版本号下，Z 值最大的版本**

  也就是说，假设你在使用 `0.37.0` 版本，但是你看到了 `0.37.1` 发布了，那么无脑上 `0.37.1` 就对了

## 升级到 0.39.x 版本

* 增加了对任务型代码通用的 XJob 工具类，详见文档末尾的 XJob 章节

* (0.39.1) 使用毫秒 Epoch 作为 XJob 的时间戳类型

* (0.39.2) XJob 工具类在开始和结束任务时都会产生日志条目

## 升级到 0.38.x 版本

* 增加了对 JVM 系统属性 和 环境变量的支持

  现在可以使用 `java -Dlogtube.xxxxx=xxxxx` 的格式，或者环境变量 `logtube.xxxxx=xxxxx` 的格式，对 `logtube` 配置文件参数进行覆盖。

* (0.38.1) 增加了 `x-access` 日志的 `x_remote_addr` 支持

## 升级到 0.37.x 版本

* 新增了生命周期日志，用以记录项目的启动事件 和 logtube 配置文件的重载事件

  只要 `logtube.file.topics` 为 `ALL`（也就是说，文件日志输出不额外过滤一遍主题）就可以无缝接入任何新增的主题

## 升级到 0.36 版本

* `IMutableEvent` 增加了一些 `x` 开头的方法，封装了一些常用的 `extra` 字段

* 增加了 `exception_stack` 字段，异常的堆栈信息会放入这个字段，允许全文搜索

* 所有输出改为了默认不开启，对于要启动的输出，比如 `console`, `file` (以及不再建议使用的 `file-plain`, `file-json`, `redis` 等)，需要写明 `enabled=true`

  比如

    ```properties
    logtube.file.enabled=true
    ```

* 增加了自动日志切分功能

  参考配置文件中的 `logtube.rotation.mode`, `logtube.rotation.keep`

  **注意** 如果先前使用了系统的 `logrotate`，则信号文件 `/tmp/xlog.reopen.txt` 可能已经从属于 `root`用户，如果 Java 进程执行在非 `root`
  用户下，则建议修改 `logtube.file.signal` 文件（以及 `file-json` 和 `file-plain` 的 `signal`)为新文件名，比如 `/tmp/xlog.reopen2.txt`

* 增加了 HTTP 路径和方法例外（用以忽略 健康检查 产生的访问日志）

  参考配置文件中的 `logtube.filter.http-ignores` 字段

  https://github.com/logtube/logtube-java/blob/master/docs/after-0.33/logtube.properties

  https://github.com/logtube/logtube-java/blob/master/docs/after-0.33/logtube.yml

## 升级到 0.35 版本

* 增加了 `crsrc` 字段，用于互相调用时，声明自身的身份，并保留在日志内容里

  `dubbo` 和 `rocketmq` 已经做了处理，但是如果在代码中使用了手动 HTTP 调用，需要补充以下代码，以将自身的 `project` 作为 `X-Correlation-Src` 头传递出去

    ```java
    conn.setRequestProperty(LogtubeConstants.HTTP_CRSRC_HEADER, Logtube.getProcessor().getProject());
    ```

## 升级到 0.34 版本

* 支持 dubbo 2.7 + 版本，需要作出如下修改

    ```properties
    dubboLogConsumerFilter=io.github.logtube.dubbo27.LogtubeDubboConsumerFilter
    dubboLogProviderFilter=io.github.logtube.dubbo27.LogtubeDubboProviderFilter
    ```

## 升级到 0.33 版本

* 合并 `file-plain` 和 `file-json`，参考以下说明进行修改:

  **Properties 格式**

    * 删除 `logtube.file-json` 相关字段
    * 删除 `logtube.file-plain` 相关字段
    * 增加 `logtube.file` 字段如下:

        ```
        # 开启日志文件
        logtube.file.enabled=true
        # 日志文件输出包含所有主题（仍然受制于全局过滤器）
        logtube.file.topics=ALL
        # 日志文件重新打开信号文件，用于 logrotate
        logtube.file.signal=/tmp/xlog.reopen.txt
        # 日志文件路径
        logtube.file.dir=logs
        # 日志子文件夹，除了 trace 和 debug 日志进入 others 子文件夹，剩下的全部进入 xlog 子文件夹
        logtube.file.subdir-mappings=ALL=xlog,trace=others,debug=others
        ```

  **YAML 格式**

    * 删除 `file-plain` 字段
    * 删除 `file-json` 字段
    * 新增 `file` 字段如下:

        ```yaml
        logtube:
          # ...
          file:
            # 开启日志文件
            enabled: true
            # 日志文件输出包含所有主题（仍然受制于全局过滤器）
            topics: ALL
            # 日志文件重新打开信号文件，用于 logrotate
            signal: /tmp/xlog.reopen.txt
            # 日志文件路径
            dir: logs
            # 日志子文件夹，除了 trace 和 debug 日志进入 others 子文件夹，剩下的全部进入 xlog 子文件夹
            subdir-mappings: ALL=xlog,trace=others,debug=others
        ```

* 使用 fatal 级别

  除了现有的级别之外，新加入了 `fatal` 级别，用以表示影响系统正常使用的错误。

  输出到 `fatal` 级别的日志为不可忽略的错误日志，一般会触发报警。

    ```java
    private static final IEventLogger LOGGER = Logtube.getLogger(XXXX.class);

    LOGGER.fatal("This is a FATAL message");
    ```

* 使用 warn 级别

  先前 `warn` 级别是被合并进入 `info` 的，之后，`warn` 级别重新启用。

    * 修改 `logtube.topic-mappings`，移除 `warn=info` 这一条重命名规则，保留其他规则

    * 原先输出在 `error` 级别的，不紧急的业务异常应该改为此级别

* 使用 XAudit 输出审计日志

  新增了一个 `x-audit` 主题用以汇总审计日志

  目前预置了一些字段，可以使用如下方式调用

    ```java
    private static final IEventLogger LOGGER = Logtube.getLogger(XXXX.class);

     XAudit.create(LOGGER)
            .setUserCode("2020020201")
            .setUserName("刘德华")
            .setIP("10.10.10.10")
            .setAction("some_action")                   // 操作标识，用于对操作分类 比如 "refund_order" 这样
            .setActionDetail("hello, world, dubababu")  // 操作详情，用于记录操作的细节
            // 等各种 Setter
            .commit() // 最后记得调用 commit
    ```

# 安装使用

1. 添加 Maven 依赖项

```xml

<dependency>
    <groupId>io.github.logtube</groupId>
    <artifactId>logtube</artifactId>
    <version>0.38.0</version>
</dependency>
```

2. 移除 `logback-classic` 和 `xlog`

由于 Logtube Java SDK 已经实现了 `SLF4J` 接口，因此移除现有的 `logback-classic` 或其他 `SLF4J` 实现。

一些快速开发包，出于简化开发流程的目的，会内嵌 `logback-classic` 依赖，但其本质上仍然只使用了 `SLF4J`接口，如 `spring-boot-starter-web`。这种情况下可以使用 `exclusion`
移除内嵌的 `logback-classic`。

**最终需要确保执行 `mvn dependency:tree` 不会出现 `logback-classic`**

参见：https://github.com/logtube/logtube-demo/blob/master/app/pom.xml

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>

    <exclusions>
        <exclusion>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

# 配置项目

##### 1. 移除所有的 `logback.xml` 文件

##### 2. 添加 `logtube.properties` 或者 `logtube.yml` 文件

和 Logback 类似，Logtube Java SDK 会从 `classpath` 载入名为 `logtube.properties` 或者 `logtube.yml` 的配置文件。

`yml` 文件会映射为 `Properties` 对象，**数组** 等价于 **逗号分隔的字符串**

**`0.33` 以及之后版本参考以下文件**

https://github.com/logtube/logtube-java/blob/master/docs/after-0.33/logtube.properties

https://github.com/logtube/logtube-java/blob/master/docs/after-0.33/logtube.yml

**`0.33` 之前版本参考以下文件**

https://github.com/logtube/logtube-java/blob/master/docs/before-0.33/logtube.properties

https://github.com/logtube/logtube-java/blob/master/docs/before-0.33/logtube.yml

下载到 `main/resources`，或者对应环境的子文件夹，注意修改 **项目名**，**项目环境名** 和 **全局主题过滤器**，并按照需要打开或关闭 一些输出。

对于生产环境和预生产环境，建议关闭 console 和 remote 输出。

**配置文件格式**

配置文件格式在 0.8 版本之后经过一次修改

* 星号 `*` 关键字，改为大写 `ALL` 关键字
* `logtube.topics` 字段，重命名为 `logtube.topics.root`

**多配置文件**

如果想要使用多个配置文件，按环境切换。可以使用以下方法：

```properties
# 文件：logtube.properties
logtube.config-file=logtube-dev.properties # 此处可以通过启用 resources filtering 和 ${} 占位符来通过 pom.xml 中的 Profile properties 进行切换
# 文件：logtube-dev.properties
#
# dev 环境的配置内容
# ...
# 文件：logtube-test.properties
#
# test 环境的配置内容
# ...
```

**使用 Apollo**

如果想要使用 Apollo，需要在 Apollo 上配置一个叫 `logtube` 的命名空间，内部填写 Properties 格式的配置项。同时项目内保存如下配置文件：

```properties
# 文件：logtube.properties
logtube.config-file=APOLLO
```

**【注意】 当前版本使用apollo，需要把app.id和apollo.meta配置放到META-INF/app.properties中。**

**使用环境变量**

`logtube.properties` 中，允许使用 `${KEY}` 语法访问环境变量

##### 3. HTTP访问日志配置

###### 3.1 常规Web项目，修改 `web.xml`，添加 `LogtubeHttpFilter`

```xml

<filter>
    <filter-name>xLogFilter</filter-name>
    <filter-class>io.github.logtube.http.LogtubeHttpFilter</filter-class>
</filter>

<filter-mapping>
<filter-name>xLogFilter</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>
```

###### 3.2 Spring Boot 项目，使用 `FilterRegistrationBean` 注册过滤器，注意调整过滤器顺序

```java
@Bean
public FilterRegistrationBean xlogFilter(){
        FilterRegistrationBean<LogtubeHttpFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new LogtubeHttpFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE+1);
        return bean;
        }
```

##### 4. Dubbo访问日志配置

**Dubbo 2.7 以下**

在src/main/resources/META-INF/dubbo目录下添加文件com.alibaba.dubbo.rpc.Filter。文件内容：

```properties
dubboLogConsumerFilter=io.github.logtube.dubbo.LogtubeDubboConsumerFilter
dubboLogProviderFilter=io.github.logtube.dubbo.LogtubeDubboProviderFilter
```

**Dubbo 2.7 以上 (logtube 0.34 以上版本支持）**

```properties
dubboLogConsumerFilter=io.github.logtube.dubbo27.LogtubeDubboConsumerFilter
dubboLogProviderFilter=io.github.logtube.dubbo27.LogtubeDubboProviderFilter
```

**【注意】 如果META-INF、dubbo目录不存在则需要手工创建，如果com.alibaba.dubbo.rpc.Filter已经存在则在文件中追加上面的内容。**

##### 5. Redis访问日志监控

Logtube重写了Client类，使用LogtubeJedisCluster继承JedisCluster，LogtubeJedisPool继承JedisPool，修改了内部对Client的使用。

操作步骤：

1. 在代码中搜索JedisCluster，使用LogtubeJedisCluster替换JedisCluster；
2. 在代码中搜索JedisPool，使用LogtubeJedisPool替换JedisPool；
3. 若代码中存在直接使用new Jedis()的地方，使用LogtubeJedis替换Jedis；
4. 验证基本功能。

##### 6. 数据库操作日志配置

###### 6.1 添加druid过滤器配置

在`src/main/resources/META-INF`目录下添加`druid-filter.properties`文件，文件内容：

```properties
druid.filters.xLogSql=io.github.logtube.druid.LogtubeDruidFilter
```

**【注意】 如果META-INF不存在则需要手工创建。**

###### 6.2 在java或配置文件中引用过滤器

设置`druid.filters`属性，可以通过java代码或配置文件

```java
DruidDataSource ds=new DruidDataSource();
        ds.setFilters("xLogSql");

//也可以在启动类中添加
        System.setProperty("druid.filters","xLogSql");
```

或

```properties
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" 
init-method="init" destroy-method="close"> 
<property name="filters" value="xLogSql" /> 
<property name="proxyFilters"> 
<list>
<ref bean="xLogSql" /> 
</list>
</property>
</bean>
<bean id="xLogSql" class="io.github.logtube.druid.LogtubeDruidFilter"> 

```

##### 7. CRID配置

如果是在代码中发起Http请求，则需要在创建连接时为HttpURLConnection添加Request Property，Key:
LogtubeConstants.HTTP_CRID_HEADER，value：Logtube.getProcessor().getCrid()。

```java
  private HttpURLConnection openHttpURLConnection(URL url,ClientRequest clientRequest,String method)throws IOException{
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection conn;
        conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type",clientRequest.getContentType());
        conn.setRequestProperty("User-Agent",clientRequest.getUserAgent());
        conn.setRequestProperty(LogtubeConstants.HTTP_CRID_HEADER,Logtube.getProcessor().getCrid());
        conn.setRequestProperty(LogtubeConstants.HTTP_CRSRC_HEADER,Logtube.getProcessor().getProject());
        return conn;
```

##### 8. XXL-JOB配置

使用 `LogtubeXxlJobSpringExecutor` 替换 `XxlJobSpringExecutor`，前者在 `JobHandler` 运行前后添加了对应的 `crid` 管理

##### 9. 线程池配置

在业务代码中使用 `ThreadPoolExecutor` 需要用 `LogtubeThreadPoolExecutor` 进行代替，才能够使得线程池中的线程能够正确地设置 CRID

# 使用 Logger

Logtube 和传统 Logger 最大的区别在于，Logtube 使用 **主题** 这一概念，它扩展了传统的日志等级（`info`, `debug` 等），可以包含用户自定义的字符串。

比如，业务逻辑可以使用 `info` 主题来进行常规日志输出，Logtube 内置的 HTTP Filter 使用 `x-access` 主题来记录请求，二者为并列关系。

##### 1. 使用传统的 SLF4J Logger

```java
Logger logger=LoggerFactory.getLogger(LogtubeTest.class);
        logger.info("hello world");
        logger.warn("warn test");
        logger.trace("hello world {}","222");
```

##### 2. 使用 IEventLogger

```java
IEventLogger logger=Logtube.getLogger(LogtubeTest.class);

// 传统纯文本日志
        logger.info("hello world");

// 传统纯文本日志（带关键字，生产环境要求 INFO 必须有关键字）
        logger.keyword("关键字1","关键字2").info("hello world");
        logger.withK("关键字1","关键字2").info("hello world"); // 等价写法

// 使用 extra 字段的结构化日志，需要用 commit() 做结束
        logger.topic("custom-topic").extras("key1","val1","key2","val2").message("hello world").commit();
```

**IEventLogger** 新增了一个新的传统级别 `fatal`，只有影响系统可用性的高级别错误，才应该输出到这个级别

##### 3. 使用 Logtube 静态方法

Logtube 类中包含所有以上提及的静态方法。

##### 4. 使用 XPerf

XPerf 可以用于对某个操作进行计时，便于性能分析

```java
// 首先，必须为当前类创建 IEventLogger，比如
private static final IEventLogger logger=Logtube.getLogger(LogtubeTest.class);

private void someMethod(){
        // 然后创建 XPerfCommitter
        XPerfCommitter committer=XPerf.create(logger)
        .setAction("some_action")
        .setActionDetail("some action detail");
        // 执行某个耗时操作
        Thread.sleep(2000);
        // 设置某个值
        committer.setValueInteger(100);
        // 提交 XPerfCommitter
        committer.commit()
        }
```

##### 5. 使用 XAudit

```java
// 首先，必须为当前类创建 IEventLogger，比如
private static final IEventLogger logger=Logtube.getLogger(LogtubeTest.class);

private void someMethod(){
        XAudit.create(logger)
        .setUserCode("2020020201")
        .setUserName("刘德华")
        .setIP("10.10.10.10")
        .setAction("some_action")
        // 等各种 Setter
        .commit() // 最后记得调用 commit
        }
```

##### 6. 使用 XJob

**警告，请使用 0.39.1 SDK, 0.39.0 SDK 存在问题无法产生有效的 XJob 日志**

* 升级 SDK 到 0.39.x 之后的写法

```java
// 0.39.1 创建 Commiter 同时指定任务名称，比如 UpdateUserTicketJob
XJobCommitter c=XJob.create(eventLogger, /* jobName = */ "sleep_1s_job");
// 0.39.2 创建 Commiter 同时指定任务名称，比如 UpdateUserTicketJob
XJobCommitter c=XJob.create(eventLogger, /* jobName = */ "sleep_1s_job", /* jobId = */ "xxxx-xxxx-xxx-xxx-xxx");

// 按照需要添加关键字
        c.addKeyword("time1s","something else")
        // 开始计时
        .markStart();

// 执行耗时的任务
        Thread.sleep(1000);

// 结束计时
        c.markEnd()
        // 记录结果
        .setResult(/* success = */ true, /* message = */ "sleep succeeded")
        // 提交
        .commit();
```

* 在 SDK 0.39.x 之前的写法

```java
IEventLogger logger=Logtube.getLogger(LogtubeTest.class);
        logger
        // 固定为 "job"
        .topic("job")
        // 任务名称
        .extra("job_name","SomethingJob")
        // 任务开始时间
        .extra("started_at",System.currentTimeMillis())
        // 任务结束时间
        .extra("ended_at",currentTimeMillis())
        // 任务持续时间（毫秒）
        .extra("duration",200)
        // 任务执行结果 ok 或者 failed 两个值选一
        .extra("result","ok")
        // 任务执行返回的详细文本信息，可供索引查询
        .message("this is a message")
        // 提交日志
        .commit();
```

# Logtube较xlog的变动

Logtube 内置一些常用的过滤器和工具，先前使用 XLog 的用户需要更新类名和引用

|XLog | Logtube | |:--|:--| | net.landzero.xlog.druid.XLogFilter | io.github.logtube.druid.LogtubeDruidFilter| |
net.landzero.xlog.dubbo.XLogConsumerFilter | io.github.logtube.dubbo.LogtubeDubboConsumerFilter| |
net.landzero.xlog.dubbo.XLogProviderFilter | io.github.logtube.dubbo.LogtubeDubboProviderFilter| |
net.landzero.xlog.http.XLogFilter | io.github.logtube.http.LogtubeHttpFilter| |
net.landzero.xlog.mybatis.XLogInterceptor | io.github.logtube.mybatis.LogtubeMybatisFilter| |
net.landzero.xlog.perf.XPerf | io.github.logtube.perf.XPerf| |net.landzero.xlog.redis.XLogJedis |
io.github.logtube.redis.LogtubeJedis| | net.landzero.xlog.redis.XLogJedisPool |
io.github.logtube.redis.LogtubeJedisPool| |net.landzero.xlog.redis.XLogJedisCluster |
io.github.logtube.redis.LogtubeJedisCluster| | net.landzero.xlog.XLog | io.github.logtube.Logtube|

获取 CRID

|XLog | Logtube | |:--|:--| | XLog.crid() | Logtube.getProcessor().getCrid()|

HTTP Header 常量 |XLog | Logtube | |:--|:--| |Constants.HTTP_CRID_HEADER|LogtubeConstants.HTTP_CRID_HEADER|
