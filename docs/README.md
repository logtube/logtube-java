# 升级日志

## 升级到 0.36 版本

* 所有输出改为了默认不开启，对于要启动的输出，需要写明 `enabled=true`

    比如

    ```properties
    logtube.file.enabled=true
    ```

* 增加了自动日志切分功能

    参考配置文件中的 `logtube.rotation.mode`, `logtube.rotation.keep`

* 增加了 HTTP 路径和方法例外（用以忽略 健康检查 产生的访问日志）

    参考配置文件中的 `logtube.filter.http-ignores` 字段

https://github.com/logtube/logtube-java/blob/master/docs/after-unified-file-output/logtube.properties
https://github.com/logtube/logtube-java/blob/master/docs/after-unified-file-output/logtube.yml

## 升级到 0.35 版本

0.35 版本增加了 `crsrc` 字段，用于互相调用时，声明自身的身份，并保留在日志内容里

`dubboe` 和 `rocketmq` 已经做了处理，但是如果在代码中使用了手动 HTTP 调用，需要补充以下代码，以将自身的 `project` 作为 `X-Correlation-Src` 头传递出去

```java
conn.setRequestProperty(LogtubeConstants.HTTP_CRSRC_HEADER, Logtube.getProcessor().getProject());
```

## 升级到 0.34 版本

从 0.34 开始，支持 dubbo 2.7 + 版本，需要作出如下修改

```properties
dubboLogConsumerFilter=io.github.logtube.dubbo27.LogtubeDubboConsumerFilter
dubboLogProviderFilter=io.github.logtube.dubbo27.LogtubeDubboProviderFilter
```

## 升级到 0.33 版本

### 1. 修改 logtube.properties 或者 logtube.yml 文件

`file-plain` 和 `file-json` 合并为一套配置了，参考以下说明进行修改。

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

上述配置基本上不需要进行修改

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

上述配置基本上不需要进行修改

### 2. 使用 fatal 级别

除了现有的级别之外，新加入了 `fatal` 级别，用以表示影响系统正常使用的错误。

输出到 `fatal` 级别的日志为不可忽略的错误日志，一般会触发报警。

```java
private static final IEventLogger LOGGER = Logtube.getLogger(XXXX.class);

LOGGER.fatal("This is a FATAL message");
```

### 3. 使用 warn 级别

先前 `warn` 级别是被合并进入 `info` 的，之后，`warn` 级别重新启用。

* 修改 `logtube.topic-mappings`，移除 `warn=info` 这一条重命名规则，保留其他规则

原先输出在 `error` 级别的，不紧急的业务异常应该改为此级别。

### 4. 使用 XAudit 输出审计日志

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


# Maven 依赖

1. 添加 Maven 依赖项

```xml
<dependency>
    <groupId>io.github.logtube</groupId>
    <artifactId>logtube</artifactId>
    <version>0.33</version>
</dependency>
```

2. 移除 `logback-classic` 和 `xlog`

由于 Logtube Java SDK 已经实现了 `SLF4J` 接口，因此移除现有的 `logback-classic` 或其他 `SLF4J` 实现。

一些快速开发包，出于简化开发流程的目的，会内嵌 `logback-classic` 依赖，但其本质上仍然只使用了 `SLF4J`接口，如 `spring-boot-starter-web`。这种情况下可以使用 `exclusion` 移除内嵌的 `logback-classic`。

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

https://github.com/logtube/logtube-java/blob/master/docs/after-unified-file-output/logtube.properties

https://github.com/logtube/logtube-java/blob/master/docs/after-unified-file-output/logtube.yml

**`0.33` 之前版本参考以下文件**

https://github.com/logtube/logtube-java/blob/master/docs/before-unified-file-output/logtube.properties

https://github.com/logtube/logtube-java/blob/master/docs/before-unified-file-output/logtube.yml

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
public FilterRegistrationBean xlogFilter() {
    FilterRegistrationBean<LogtubeHttpFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new LogtubeHttpFilter());
    bean.addUrlPatterns("/*");
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
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
DruidDataSource ds = new DruidDataSource();
ds.setFilters("xLogSql");

//也可以在启动类中添加
System.setProperty("druid.filters", "xLogSql");
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

如果是在代码中发起Http请求，则需要在创建连接时为HttpURLConnection添加Request Property，Key: LogtubeConstants.HTTP_CRID_HEADER，value：Logtube.getProcessor().getCrid()。

```java
  private HttpURLConnection openHttpURLConnection(URL url, ClientRequest clientRequest, String method) throws IOException {
    HttpURLConnection.setFollowRedirects(true);
    HttpURLConnection conn;
    conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod(method);
    conn.setRequestProperty("Content-Type", clientRequest.getContentType());
    conn.setRequestProperty("User-Agent", clientRequest.getUserAgent());
    conn.setRequestProperty(LogtubeConstants.HTTP_CRID_HEADER, Logtube.getProcessor().getCrid());
    conn.setRequestProperty(LogtubeConstants.HTTP_CRSRC_HEADER, Logtube.getProcessor().getProject());
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
Logger logger = LoggerFactory.getLogger(LogtubeTest.class);
logger.info("hello world");
logger.warn("warn test");
logger.trace("hello world {}", "222");
```

##### 2. 使用 IEventLogger

```java
IEventLogger logger = Logtube.getLogger(LogtubeTest.class);

// 传统纯文本日志
logger.info("hello world");

// 传统纯文本日志（带关键字，生产环境要求 INFO 必须有关键字）
logger.keyword("关键字1", "关键字2").info("hello world");
logger.withK("关键字1", "关键字2").info("hello world"); // 等价写法

// 使用 extra 字段的结构化日志，需要用 commit() 做结束
logger.topic("custom-topic").extras("key1", "val1", "key2", "val2").message("hello world").commit();
```

**IEventLogger** 新增了一个新的传统级别 `fatal`，只有影响系统可用性的高级别错误，才应该输出到这个级别

##### 3. 使用 Logtube 静态方法

Logtube 类中包含所有以上提及的静态方法。

##### 4. 使用 XPerf

XPerf 可以用于对某个操作进行计时，便于性能分析

```java
// 首先，必须为当前类创建 IEventLogger，比如
private static final IEventLogger logger = Logtube.getLogger(LogtubeTest.class);

private void someMethod() {
  // 然后创建 XPerfCommitter
  XPerfCommitter committer = XPerf.create(logger)
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
private static final IEventLogger logger = Logtube.getLogger(LogtubeTest.class);

private void someMethod() {
    XAudit.create(logger)
        .setUserCode("2020020201")
        .setUserName("刘德华")
        .setIP("10.10.10.10")
        .setAction("some_action")
        // 等各种 Setter
        .commit() // 最后记得调用 commit
}
```
# Logtube较xlog的变动

Logtube 内置一些常用的过滤器和工具，先前使用 XLog 的用户需要更新类名和引用

|XLog | Logtube |
|:--|:--|
| net.landzero.xlog.druid.XLogFilter |  io.github.logtube.druid.LogtubeDruidFilter|
| net.landzero.xlog.dubbo.XLogConsumerFilter |  io.github.logtube.dubbo.LogtubeDubboConsumerFilter|
| net.landzero.xlog.dubbo.XLogProviderFilter |  io.github.logtube.dubbo.LogtubeDubboProviderFilter|
| net.landzero.xlog.http.XLogFilter |  io.github.logtube.http.LogtubeHttpFilter|
| net.landzero.xlog.mybatis.XLogInterceptor |  io.github.logtube.mybatis.LogtubeMybatisFilter|
| net.landzero.xlog.perf.XPerf |  io.github.logtube.perf.XPerf|
|net.landzero.xlog.redis.XLogJedis  |  io.github.logtube.redis.LogtubeJedis|
| net.landzero.xlog.redis.XLogJedisPool |  io.github.logtube.redis.LogtubeJedisPool|
|net.landzero.xlog.redis.XLogJedisCluster  |  io.github.logtube.redis.LogtubeJedisCluster|
| net.landzero.xlog.XLog |  io.github.logtube.Logtube|

获取 CRID

|XLog | Logtube |
|:--|:--|
| XLog.crid() |  Logtube.getProcessor().getCrid()|

HTTP Header 常量
|XLog | Logtube |
|:--|:--|
|Constants.HTTP_CRID_HEADER|LogtubeConstants.HTTP_CRID_HEADER|
