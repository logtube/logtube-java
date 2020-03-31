Guo Y.K. 2019年04月23日

# Maven 依赖

1. 添加 Maven 依赖项

```xml
<dependency>
    <groupId>io.github.logtube</groupId>
    <artifactId>logtube</artifactId>
    <version>0.32</version>
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

参见：https://github.com/logtube/logtube-java/blob/master/src/test/resources/logtube.properties

参见：https://github.com/logtube/logtube-java/blob/master/src/test/resources/logtube.yml

下载到 `main/resources`，或者对应环境的子文件夹，注意修改 **项目名**，**项目环境名** 和 **全局主题过滤器**，并按照需要打开或关闭 一些输出。

运行于 Kubernetes 集群的项目建议关闭所有 file 输出，只使用 remote 输出。对于生产环境和预生产环境，建议关闭 console 和 remote 输出。

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
    bean.setFilter(new LogtubeDruidFilter());
    bean.addUrlPatterns("/*");
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    return bean;
}
```

##### 4. Dubbo访问日志配置
在src/main/resources/META-INF/dubbo目录下添加文件com.alibaba.dubbo.rpc.Filter。文件内容：

```properties
dubboLogConsumerFilter=io.github.logtube.dubbo.LogtubeDubboConsumerFilter
dubboLogProviderFilter=io.github.logtube.dubbo.LogtubeDubboProviderFilter
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

##### 2. 使用 LogtubeLogger

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

##### 3. 使用 Logtube 静态方法

Logtube 类中包含所有以上提及的静态方法。

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
