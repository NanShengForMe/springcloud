# Spring Cloud

[TOC]

## 查看版本依赖关系

https://start.spring.io/actuator/info 访问这个链接查看SpringCloud与SpringBoot的依赖关系.

## 搭建Project

使用Maven基本骨架创建-site父工程项目

## 三个注册中心异同点

| 组件名    | 语言 | CAP  | 服务健康检查 | 对外暴露接口 | SpringCloud继承 |
| --------- | ---- | ---- | ------------ | ------------ | --------------- |
| Eureka    | java | AP   | 可配支持     | HTTP         | 已集成          |
| Zookeeper | Java | CP   | 支持         | 客户端       | 已集成          |
| Consul    | Go   | CP   | 支持         | HTTP/DNS     | 已集成          |

CAP:

​	C:Consistency（强一致性）

​	A:Availability（可用性）

​	P:Partition tolerance（分区容错性）（分布式架构必须保证）

CAP理论关注粒度十数据，而不是整体系统设计。

最多只能同时较好的满足两个。

CAP理论核心是：一个分布式系统不可能同时很好的满足一致性、可用性和分区容错性这三个需求，因此，根据CAP原理将NoSql数据库分成了满足CA原则、满足CP原则和满足AP原则三大类。

CA - 单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大

CP - 满足一致性，分区容忍的系统，通常性能不是特别高

AP - 满足可用性，分区容忍的系统，通常可能对一致性要求低一些

## Ribbon

netflix实现的一套***客户端***负载均衡工具。

我们很容易使用Ribbon实现自定义的负载均衡算法。

目前该项目也已进入维护模式。

能干嘛：

LB（负载均衡）

1. 集中式LB

   即在服务的消费方和提供方之间使用独立的LB设施（可以是硬件，如F5，也可以是软件，如Nginx），由该设施负责把访问请求通过某种策略转发至服务的提供方。

2. 进程内LB

   将LB的逻辑集成到消费方，消费方从服务注册中心获知有哪些地址可用，然后自己在从这些地址中选择出一个合适的服务器。

   Ribbon就属于进程内LB，它只是一个类库，集成于消费方进程，消费方通过它来获取到服务提供方的地址。

​    负载均衡，简单来说就是将用户的请求平摊的分配到多个服务器上，从而达到系统的HA（高可用）。常见的负载均衡软件Nginx，LVS，硬件F5等。

Ribbon本地负载均衡客户端 VS Nginx负载均衡区别：

Nginx是服务器负载均衡，客户端所有请求都会交给nginx，然后由nginx实现转发请求。即负载均衡是由服务端实现的。

Ribbon本地负载均衡，在调用微服务接口的时候，会在注册中心上获取注册信息服务列表后缓存到JVM本地，从而在本地实现RPC远程服务调用技术。



Ribbon在工作时分为两步：

第一步先选择EurekaServer，它在优先选择在同一个区域内负载较少的server

第二步再根据用户指定策略，再从server取到的服务注册列表中选择一个地址。

其中Ribbon提供了多种策略：比如轮询、随机和根据响应时间加权。



轮询负载均衡算法：rest接口第几次请求数%服务器集群数量=实际调用服务器位置下标，每次服务重启后rest接口计数器从1开始。

## OpenFeign

Feign与OpenFeign区别

| Feign                                                        | OpenFeign                                                    |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Feign是SpringCloud组件中的一个轻量级RESTful的HTTP服务客户端。Feign内置的Ribbon，用来做服务端的负载均衡，去调用注册中心的服务。Feign的使用方式是：使用Feign的注解定义接口，调用这个接口，就可以调用服务中策中心的服务。 | OpenFeign是SpringCloud在Feign的基础上支持了SpringMVC的注解，如@RequestMapping等等。OpenFeign的@FeignClient可以解析SpringMVC的@RequestMapping注解下的接口，通过动态代理的方式生成实现类，实现类中做负载均衡并调用其他服务。 |

OpenFeign的超时控制：

```yml
ribbon:
  #指的是建立连接后从服务器读取到可用资源所用的时间
  ReadTimeout: 5000
  #指的是建立连接所用时间，适用于网络状况正常的情况下，两端连接所用时间
  ConnectTimeout: 5000
```

## Hystrix断路器

分布式面临的问题：对于复杂的分布式体结构中的应用程序数十个依赖关系，每个依赖关系在某些时候将不可避免的失败。

Hystrix是什么：是一个分布式系统的延迟和容错的开源库，在分布式系统里，许多以来不可避免的会出现调用失败，比如超时、异常等，Hystrix能保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。

”断路器“本身是一种开关装置，当某个服务单元发生故障后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期的可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间的、不必要的占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。

**能干嘛？**

最重要的三点：服务降级、服务熔断、接近实时的监控

- 服务降级（fallback）

  服务器忙，请稍后再试，不让客户端等待并立刻返回一个友好的提示，fallback。

  哪些情况会触发降级：程序运行异常、超时、服务熔断触发服务降级、线程池/信号量打满也会导致服务降级。

- 服务熔断（break）

  类比保险丝达到最大服务访问，直接拒绝访问，拉闸限电，然后调用服务降级的方法并提示友好返回提示。

  就是保险丝，服务的降级->进而熔断->恢复调用链路

- 服务限流（flowlimit）

  秒杀等高并发操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行

**<u>示例测试（JMeter压力测试）：当使用200个线程100次，总计20000次并发访问某一接口，其余接口均会导致卡顿（原因：tomcat的默认工作线程数被打满了，没有多余的线程来分解压力和处理）。</u>**

最终结论：以上服务还是8001自测，加入此时外部的消费者80也来访问，那消费者只能干等，最终导致消费者80不满意，服务端8001直接被拖死。



如何解决？解决的要求？（解决的维度）

- 超时导致服务器变慢（变慢）——超时不再等待

- 出错（宕机或程序出错）——出错要有兜底

解决：

- 对方服务器（8001）超时了，调用者（80）不能一直卡死等待，必须有服务降级
- 对方服务器（8001）down机了，调用者（80）不能一直卡死等待，必须有服务降级
- 对方服务（8001）OK，调用者（80）自己故障或有自我要求（自己的等待时间小于服务提供者），自己处理降级

### 服务降级

降级的配置：以注解替代配置：@HystrixCommand

8001自身找问题：

​			设置自身调用时间的峰值，峰值内可以正常运行，超过了要有兜底的方法处理，做服务降级fallback

```java
@HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })

一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法;
规定线程超时时间3s。
配合@EnableCircuitBreaker注解开启服务降级。
```

```
@HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        int timerNumber = 5;
          // int i = 1/0;
	        try {
	            TimeUnit.SECONDS.sleep(timerNumber);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
        return "线程池：" + Thread.currentThread().getName() + "    paymentInfo_TimeOut,id:    " + id + "\t" + "=_=" + "耗时" + timerNumber + "秒钟";
    }
```

无论是运行异常还是超时异常，都会做降级，兜底方案都是paymentInfo_TimeOutHandler。



**<u>80消费端，也可以更好的保护自己，自己可以进行客户端降级保护。（一般降级用于客户端）</u>**

```yml
feign:
  hystrix:
    enabled: true
```

```java
@EnableHystrix
```

以上两步开启通过OpenFeign调用的降级。

```java
@GetMapping(value = "/consumer/payment/hystrix/timeout/{id}", produces = "application/json;charset=UTF-8")
@HystrixCommand(fallbackMethod = "paymentTimeOutFallBackMethod", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
})
public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
    return paymentHystrixService.paymentInfo_Timeout(id);
}

public String paymentTimeOutFallBackMethod() {
    return "我是消费者80，对方支付系统繁忙请10秒之后再试试或者自己运行出错，请检查自己，┭┮﹏┭┮";
}
```

**<u>以上使用服务降级的方式所出现的问题：</u>**

1. 每个方法都要单独指定，导致代码膨胀

   global fallback？

   解决：feign接口系列；@DefaultPropertiest(defaultFallBack = "")、@HystraixCommand、降级的方法

   没有特别指明就用统一的降级方法。

   @DefaultPropertiest(defaultFallBack = ""):    1:n，除了个别重要核心业务有专属，其他普通的可以通过这个注解统一跳转处理结果页面。

   通用和独享的各自分开，避免了代码膨胀，合理减少了代码量。

2. 业务方法和异常方法耦合

   服务降级，客户端去调用服务端，碰上服务端宕机或关闭。

   我们常常遇到的异常：运行、超时、宕机。

   新建PaymentFallbackService实现Feign调用接口

```java
/**
 * 统一做服务降级处理,解耦
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月03日 14:33:00
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {

    @Override
    public String paymentInfo_OK(Integer id) {
        return "=========PaymentFallbackService  fallback   paymentInfo_OK";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "=========PaymentFallbackService  fallback   paymentInfo_Timeout";
    }
}
```

### 服务熔断

就是保险丝：服务的降级-》进而熔断-》恢复调用链路

```
熔断机制概述
熔断机制是应对雪崩效应的一种微服务链路保护机制。当删除链路中的某个微服务，出错不可用或者响应时间太长时，会对服务进行降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。
当检测到该节点微服务调用响应正常后，恢复调用链路。

在SPringCloud框架里，熔断机制通过Hystrix实现。Hystrix会监控微服务间的调用状况，当失败到达一定阈值，缺省是5秒20次调用失败，就会启动熔断机制。熔断机制的的注解是@HystrixCommand
```

```java
// =============================服务熔断=======================================

@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            // 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.enable", value = "true"),
            // 请求次数，请求次数超过峰值，熔断器将会将会从关闭变为打开
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            // 失败率达到多少后跳闸
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("========id不能为负数");
        }
        // 等价于UUID.randomUUID().toString()，并且已经去掉下划线
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能为负数，请稍后再试，┭┮﹏┭┮ id：" + id;
    }

```

测试时：多次错误以后，然后慢慢正确，发现开始不满足条件，就算是正确的访问也不能访问

**<u>服务熔断总结：</u>**

​		熔断打开：请求不再进行调用当前服务，内部设置时钟一般为MTTR(平均故障处理时间)，当打开时长达到所设时钟则进入半熔断状态；

​		熔断关闭：熔断关闭不会对服务进行熔断；

​		熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断。

涉及到断路器的三个重要参数：

1. 快照时间窗：断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒
2. 请求总数阈值：在快照时间窗内，必须满足请求总数阈值才有资格熔断。默认为20，意味着在10秒钟内，如果该Hystrix命令的调用次数不足20次，即使所有的请求都超时或其他原因失败，断路器都不会打开。
3. 错误百分比阈值：当请求总数在快照时间窗内超过了阈值，比如发生了30次调用，如果在这30次中，有15次发生了超时异常，也就是超过50%的错误百分比，在默认设定50%阈值情况下，这时候就会将断路器打开。

**<u>当断路器开启后所有的请求都不会进行转发，一段时间后（默认5秒），这时候断路器是半开状态，会让其中一个请求进行转发。如果成功，断路器则会关闭，若失败，继续开启。重复以上两步</u>**

### 服务限流

与alibaba的Sentinel联动看。

### Hystrix工作流程

![img](https://img2018.cnblogs.com/blog/1102969/201908/1102969-20190817194233549-1075974382.png)

### DashBoard监控

小心踩坑：被监控者必须要actuator功能

低版本坑：

```java
/**
 * 此配置是为了服务监控而配置，与服务容错本身无关，springcloud升级后的坑
 * ServletRegistrationBean因为springboot的默认路径不是/hystrix.stream
 * 只要在自己的项目里配置上下面的servlet就可以
 */
@Bean
public ServletRegistrationBean getServlet() {
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
}
```

## 服务网关

Zuul核心人员跳槽，已经停更，Zuul2迟迟发不出，spring的Gateway开始使用。

### GateWay概述

```
Cloud全家桶重要的组件就是网关。在1.x的版本都是采用Zuul网关，但是在2.x的版本中zuul的升级一直跳票，SpringCloud最后自己研发了一个网关替代Zuul，那就是SpringCloud gateway一句话：gateway是原Zuul1.x的替代品。

是基于Spring5.0+Springboot2.0和Project Reactor等技术开发的网关，旨在为微服务架构提供一种简单有效的统一的API路由管理方式。

为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty。

基于异步非阻塞模型。

SpringCloud Gateway的目标是提供统一的路由方式且基于Filter链的方式提供了网关基本的功能，例如：安全，监控/指标和限流。

一句话：SpringCloud Gateway使用的是WebFlux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架。
```

### 能干嘛

方向代理、鉴权、流量控制、熔断、日志监控.....

### Zuul与Gateway

在Spring Cloud F正式版本之前，Spring Cloud推荐的网关Netflix提供的Zuul：

1. Zuul1.x，是一个基于阻塞I/O的API Gateway
2. Zuul1.x基于Servlet2.5使用阻塞框架它不支持任何长连接（如WebSocket）Zuul的设计模式和Nginx教像，每次IO操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成，但是差别是Nginx用C++实现，Zuul用Java实现，而JVM本身会有第一次加载较慢的情况，是的Zuul的性能相对较差。
3. Zuul2.x理念更先进，相基于Netty非阻塞和支持长连接，但Spring Cloud目前还没有整合。Zuul2.x的性能较Zuul1.x有较大提升。在性能方面，根据官方提供的基准进行测试，Spring Cloud Gateway的RPS（每秒请求数）是Zuul的1.6倍。
4. Spring Cloud Gateway建立在SpringFramework5、Project Reactor和Spring Boot2.0之上，使用非阻塞API。
5. Spring Cloud Gateway还支持WebSocket，并且与Spring紧密集成用友更好的开发体验。

### WebFlux

```
传统的Web框架，比如说：struts2，springMVC都是基于ServletApi与servlet容器基础之上运行了。但是在servlet3.1之后有了异步非阻塞的支持。而WebFlux是一个典型的非阻塞异步框架，它的核心是基于Reactor的相关API实现的。相对于创痛的web框架来说，他可以运行在诸如Netty，Undertow及支持Servlet3.1的容器上。非阻塞+函数式编程（Spring5必须强制让你使用java8）。

Spring WebFlux是Spring5.0引入的新的响应式框架，区别于SpringMVC，他不需要依赖ServletAPI，它是完全异步非阻塞的，并且基于Reactor来实现响应式流规范。
```

### Gateway三大组件

- Route（路由）：是构建网关的基本模块，他由ID，目标URI，一系列断言和过滤器组成，如果断言为true则匹配该路由。
- Predicate（断言）：（参考Java8的java.util.function.Predicate）开发人员可以匹配HTTP请求中的所有内容（例如请求头或请求参数），**如果请求与断言相匹配则进行路由**。
- Filter（过滤器）：指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。

总结：web请求，通过一些匹配条件，定位到真正的的服务节点。并在这个转发过程的前后，进行一些精细化控制。Predicate就是匹配条件，而filter，就可以理解为一个无所不能的拦截器。有了这两个元素，再加上URI，就可以实现具体的路由。

Pre类型过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等；

Post类型过滤器可以做响应内容、响应头的修改，日志的输出，流量监控等有着非常重要的作用。

**<u>核心逻辑即：路由转发+执行过滤链</u>**

### 9527实例测试

**********************************************************

Spring MVC found on classpath, which is incompatible with Spring Cloud Gateway at this time. Please remove spring-boot-starter-web dependency.

**********************************************************

gateway中不需要web和actuator的依赖。

#### 1、yml配路由

```yml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的id，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001 #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/**  #断言，路径相匹配的进行路由

        - id: payment_routh2 #路由的id，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001
          predicates:
            - Path=/payment/lb/**
```

#### 2、代码中注入RouteLocator的Bean

```java
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // 当访问地址http://localhost:9527/guonei会被转发到http://news.baidu.com/guonei
        return builder.routes()
                .route("path_route_baidunews",
                        r -> r.path("/guonei").uri("http://news.baidu.com/guonei"))
                .route("path_route_baidunews2",
                        r -> r.path("/guoji").uri("http://news.baidu.com/guoji"))
                .build();
    }

}
```

### 通过微服务实现动态路由配置

默认情况下Gateway会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由功能。

```yml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的id，没有固定规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001 #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/**  #断言，路径相匹配的进行路由

        - id: payment_routh2 #路由的id，没有固定规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/lb/**
      # 开启从注册中心动态创建路由的功能，利用微服务名进行路由
      discovery:
        locator:
          enabled: true
```

### 常用Predicate

```
时区类：After、Before、Between
- After=2020-08-04T17:28:49.332+08:00[Asia/Shanghai]

Cookie类
带cookie访问.Cookie Route Predicate需要两个参数，一个是Cookie name，一个是正则表达式。路由规则会通过获取对应的Cookie name值和正则表达式去匹配，如果匹配上就会执行路由，如果没有匹配上则不执行 
- Cookie=chocolate,ch.p
使用curl测试：curl http://localhost:9527/payment/7 --cookie "username=zoujidi"

Head类：属性=正则表达式进行匹配
- Header=X-Request-Id, \d+ #要求请求头要有X-Request-Id属性并且值为整数的正则表达式
使用curl测试：curl http://localhost:9527/payment/7 -H "X-Request-Id:123"

Host
Method
Path
Query:查询参数、条件
...
```

### 过滤器

常用的GatewayFilter31种+Global Filter10种

```yml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的id，没有固定规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001 #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/**  #断言，路径相匹配的进行路由
            # - After=2020-08-04T16:28:49.332+08:00[Asia/Shanghai]
            # - Cookie=username,zoujidi
            - Header=X-Request-Id, \d+ #要求请求头要有X-Request-Id属性并且值为整数的正则表达式
          filter:
            - AddRequestParameter=X-Request-Id,1024 #过滤器工厂会在匹配的请求头上加一对请求头，名称为X-Request-Id值为1024
```

### 自定义过滤器

两个接口GlobalFilter，Ordered

主要用于全局日志、统一网关鉴权...

```java
@Component
@Slf4j
public class MyLogGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("=================com in MyLogGatewayFilter:" + new Date());
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if (uname == null) {
            log.info("============用户名为null，非法用户=============");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 过滤器优先级，数字越小优先级越高
        return 0;
    }
}
```

## 分布式配置中心

###  分布式面临的问题

微服务意味着要将单体应用中的业务拆分这次能一个个自服务，每个服务粒度相对较小，因此系统会出现大量的服务。由于每个服务都必须要必须的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必不可少的。

SpringCloud提供了ConfigServer来解决这个问题，我们每一个微服务都自己带着一个application.yml，上百个配置文件的管理...

### 是什么

SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器伟哥哥不同微服务应用的所有环境提供一个中心化的外部配置。

### 怎么玩

SpringCloud Config分为服务端和客户端两部分。

服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口。

客户端则是通过配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容。

### 能干嘛

- 集中管理配置文件
- 不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release
- 运行期间动态调整配置，不再需要在每个服务器部署的机器上编写配置文件，服务会向配置中心统一拉取配置自己的信息
- 当配置发生变化时，服务器不需要重启即可感知到配置的变化并应用新的配置
- 将配置信息以REST接口形式暴露

### 与GitHub整合

由于SpringCloud Config默认采用Git来存储配置文件（也有其他方式，比如支持SVN和本地文件），单推荐还是Git，而且使用的是http/https访问形式。

Label：分支（branch）

name：服务名

profiles：环境（dev/test/prod）

### Bootstrap.yml

application.yml是用户级的资源配置项

Bootstrap.yml是系统级的，优先级更加高

SpringCloud会创建一个“Bootstrap Context”，作为Spring应用的Application Context的父上下文。初始化的时候，Bootstrap Context负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的Environment。

Bootstrap属性有高优先级，默认情况下，它们不会被本地覆盖。Bootstrap context和Application Context有着不同的约定，所以增加了一个bootstrap.yml文件，保证Bootstrap Context和Application Context配置的分离。

要将Client模块下的application.yml文件改为bootstrap.yml，这是很关键的，因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml.

双亲委派模型？

### config客户端之动态刷新

步骤：

1. config客户端添加actuator依赖
2. 修改yml，暴露监控端点
3. @RefreshScope业务类Controller修改
4. 此时修改github-->3344(配置中心)-->3355客户端
5. 需要运维人员发送Post请求刷新3355：必须是post请求， curl -X POST "http://localhost:3355/actuator/refresh"
6. 改完之后发送请求刷新即可，无需重启

观察者模式？

出现问题：假设有点多个客户端（3355、3366、3377...）？每个微服务都要执行一次post请求，手动刷新？可否广播，一次通知，处处生效？想大范围的自动刷新，求方法？引出下一章

## SpringCloud Bus消息总线

对配置中心的加深和扩充：

- 分布式自动刷新配置功能
- Spring Cloud Bus配合Spring Cloud Config使用可以实现配置的动态刷新

### 是什么

Bus支持两种消息总线：RabbitMQ和Kafka

### 能干嘛

Spring Cloud Bus配合Spring Cloud Config使用可以实现配置的动态刷新

![消息](/Users/zoujidi/IdeaProjects/springcloud/doc/images/config-bus.png)

Spring Cloud Bus是用来将分布式系统的节点与轻量级消息系统连接起来的框架，**<u>它整合了Java的时间处理机制和消息中间件的功能</u>**。

Spring Cloud Bus目前只支持RabbitMQ和Kafka。

Spring Cloud Bus能管理和传播分布式系统间的消息，就像一个分布式执行器，可用于广播状态更改、事件推送等，也可以作为微服务间的通信通道。

### 什么是总线

在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个公用的消息主题，并让系统中的所有微服务实例都连上来。由于该主题中产生的消息会被所有实例监听的消费，所以称他为消息总线。在总线上的各个实例，都可以方便的广播一些需要让其他连接在该主题上的实例都知道的消息，

基本原理：ConfigClient实例都监听一个MQ中同一个topic（默认是SpringCloudBus），当同一个服务刷新数据时候，他会把这个信息放到Topic中，这样其他监听同一个Topic的服务就能够得到通知，然后去更新自身的配置。

### 设计思想

1. 利用消息总线触发一个客户端/bus/refresh，而刷新所有客户端配置
2. 利用消息总线触发服务端ConfigServer的/bus/refresh，而刷新所有客户端的配置

第二点更合适，原因如下：

- ​			打破了微服务的职责单一性，因为微服务本身是块业务模块，它不应该承担配置刷新的职责；
- ​			破坏了微服务各节点的对称性 
- ​			有一定局限性。例如，微服务在迁移时，他的网络地址常常会发生变化，此时如果想要做到自动刷新，那就会增加更多的修改

### 实例

curl -X POST "http://localhost:3344/actuator/bus-refresh" 发送到消息总线

```yml
server:
  port: 3344

spring:
  application:
    name: cloud-config-center #注册进Eureka服务器的微服务名
  cloud:
    config:
      server:
        git:
          uri: https://github.com/NanShengForMe/springcloud-config.git #配置GitHub上面git仓库的名字
          # 搜索目录
          search-paths:
            - springcloud-config
      #读取分支
      label: master

#RabbitMQ相关配置 15672是Web管理界面的端口，5672是MQ访问的端口
  rabbitmq:
    host: 49.235.50.180
    port: 5672
    username: guest
    password: guest
```

```yml
server:
  port: 3355

spring:
  application:
    name: config-client
  cloud:
    #config客户端配置
    config:
      label: master #分支名称
      name: config #配置文件名称
      profile: dev #读取后缀名称  上述三个综合：master分支上config-dev.yml的配置文件被读取http://config-3344:3344/master/config-dev.yml
      uri: http://localhost:3344 #配置中心地址

  #RabbitMQ相关配置 15672是Web管理界面的端口，5672是MQ访问的端口
  rabbitmq:
    host: 49.235.50.180
    port: 5672
    username: guest
    password: guest

#服务注册到Eureka地址
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka

#暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*" #refresh
```

一次修改，广播通知，处处生效O(∩_∩)O

### 动态刷新定点通知

不想全部通知，只想定点通知：只通知3355不通知3366

简单一句话：指定具体一个实例生效而不是全部

公式：http://localhost:配置中心端口号/actuator/bus-refresh/{destination}（destination例如config-client:3355）

/bus/refresh请求不再发送到具体的服务实例上，二十发送到config server通过destination参数类指定需要更新配置的服务或实例

## Spring Cloud Stream消息驱动

### 解决痛点

MQ（消息中间件）：

​	ActiveMQ

​	RabbitMQ 

​	RocketMQ

​	Kafka

应用部分和大数据部分应用不同MQ，那么将会存在切换、维护、开发等困难。

有没有一种新的技术诞生，让我们不再关注MQ的细节，我们只需要用一种适配绑定的方式，自动的给我们在各种MQ内切换。

Cloud Sream！

### 是什么

屏蔽底层消息中间件的差异，降低切换版本成本，统一消息的编程模型。

官方定义Spring Cloud Stream是一个构建消息驱动微服务的框架.

应用程序通过inputs或者outputs来与Spring Cloud Stream中binder对象交互,通过我们来配置binder(绑定),而Spring Cloud Stream的binder对象负责与消息中间件进行交互.所以,我们只需要搞清楚如何与Spring Cloud Stream交互就可以方便使用消息驱动的方式.

通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动.

Spring Cloud Stream为一些供应商的消息中间件产品提供了个性化的自动化配置实现,引用了发布-订阅、消费组、分区的三个核心概念.

目前仅支持RabbitMQ、Kafka.

### 设计思想

标准MQ:

- 生产者/消费者之间靠消息媒介传递信息内容========》message
- 消息必须走特定通道========〉MessageChannel
- 消息通道里的消息如何被消费呢,谁负责收发处理====》消息通道MessageChannel子接口SubscribableChannel,由MessageHanlder消息处理器所订阅

通过定义绑定器(Binder)作为中间层,实现了应用程序与消息中间件细节之间的隔离.

Binder:

​		INPUT对应生产者

​		OUTPUT对应消费者

Stream中的消息通信方式遵循了发布-订阅模式.主要使用Topic进行广播:

Topic:在RabbitMQ就是Exchange,在Kafka中就是Topic

### Spring Cloud Stream标准流程套路

![stream](/Users/zoujidi/IdeaProjects/springcloud/doc/images/Stram事件驱动.png)

Binder:很方便的连接中间件,屏蔽差异

Channel:通道,是队列Quene的一种抽象,在消息通讯系统就是实现存储和转发的媒介,通过Channel进行配置

Source和Sink:简单可理解为参照对象是Spring Cloud Stream自身,从stream发布消息就是输出,接受消息就是输入

### 分组消费与持久化

测试环境:

RabbitMQ

7001服务注册中心

8801消息生产

8802消息消费

8803消息消费

运行后有两个问题: 有重复消费问题;消息持久化问题;

**<u>8802和8803都收到了,存在重复消费问题,使用分组和持久化属性group,将其分到同一个组,存在竞争关系即可</u>**

原理:微服务应放于同一个group中,就能够保证消息只会被其中一个应用消费一次.不同的组是可以消费的,同一个组内会发生竞争关系,只有其中一个可以消费.

多数情况,生产者发送消息给某个具体微服务时只希望被消费一次,按照启动的两个应用例子,虽然他们同属一个应用,但是这个消息出现了被重复消费两次的情况.为了解决这个问题,SpringCloud Stream中提供了**消费组**的概念.

还可解决持久化(消息丢失问题).

## Spring Cloud Sleuth

### 是什么

Spring Cloud Sleuth提供了一套完整的服务跟踪解决方案.在分布式系统中提供追踪解决方案并且兼容支持了zipkin

zipkin:

http://localhost:9411/zipkin/

术语: 一条链路通过Trace Id唯一标识,Span标识发起的请求信息,各span通过parent_id关联起来 

## Spring Cloud Alibaba

随着SpringCloud Netflix项目进入维护模式,出现了SpringCloud Alibaba

阿里巴巴的Dubbo停更后出现了SpringCloud(Netflix):Eureka、Ribbon、Feign、Config、Zuul=======》2018年

Netflix停更后=====〉SpringCloud Alibaba出现了 

### 能干嘛

- **服务限流降级**：默认支持 WebServlet、WebFlux, OpenFeign、RestTemplate、Spring Cloud Gateway, Zuul, Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
- **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
- **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
- **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
- **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。。
- **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
- **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

## Spring Cloud Nacos

前四个字母为nameing和Configuration的前两个字母,最后的s为Service.

就是注册中心+配置的组合.等价于Nacos=Eureka+Config+Bus

AP框架:舍弃一致性,保证高可用.

### 能干嘛

替代Eureka做注册中心

替代Config做配置中心

### 实例

```yml
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.0:8848 #配置注册中心

management:
  endpoints:
    web:
      exposure:
        include: *
```

### 各注册中心的对比

Nacos同时支持CP和AP模式的切换.

C时所有节点在同一时间看到的数据都是一致的;而A的定义是所有的请求都会收到响应.

合适选用何种模式?

一般来说,如果不需要存储服务级别的信息且服务实例是通过nacos-client注册,并能够保持心跳上报,那么就可以选择AP模式.当前主流的服务如Spring Cloud 和 Dubbo服务都适用于AP模式,AP模式为了服务的可能性而减低了一致性,因此AP模式下只支持注册临时实例.

如果需要在服务级别编辑或者存储配置信息,那么CP是必须的,k8s服务和DNS服务则属于CP模式.CP模式下则支持注册持久化实例,此时则是以Raft协议为集群运行模式,该模式下注册实例前必须先注册服务,如果服务不存在,则返回错误.

### 作为配置中心

bootstrap优先级高于application

通过Spring Cloud原生注解@RefreshScope实现配置自动更新

在nacos中添加配置信息:Nacos中的匹配规则:Nacos中的dataid的组成格式及与SpringBoot配置文件中的规则:${prefix}-${spring.profile.active}.${file-extension}

### 作分类配置

实际问题:多环境(profile)、多项目

Namespace+Group+DataID:类似Java里面的package名和类名

最外层的Namespace是可以用于区分部署环境的,Group和DataID逻辑上区分两个目标对象.

默认情况:Namespace=public,Group=DEFAULT_GROUP,默认Cluster是DEFAULT

### Nacos的集群配置的持久化配置(重要)

nacos默认使用嵌入式的数据库实现数据的存储.所以,如果启动多个默认配置下的Nacos节点,数据存储是会存在一致性问题的.为了解决这个问题,Nacos采用的**<u>集中式存储的方式来支持集群化部署,目前只支持MySQL的存储</u>**.

Nacos支持 的三种部署方式:

- 单机模式-用于测试和单机使用
- 集群模式-用于生产环境,确保高可用.
- 多集群模式-用于多数据中心场景

nacos默认自带的是嵌入式的数据库derby,切换为mysql

预计需要1个nginx+3个nacos+1个mysql

#### 集群配置步骤

修改nacos脚本,启动时候 startup -p 3333?

## Sentinel

Hystrix:

- 需要我们程序员自己搭建监控平台
- 没有一套web界面给我们进行更加细粒度化的配置流量控制、速率控制、服务熔断、服务降级。。。。

Sentinel

- 单独一个组件，可以独立出来
- 支持界面化的细粒度统一配置

约定》配置》编码，都可以写在代码里面，但是我们本次还是大规模的学习使用配置和注解的方式，尽量少些代码

解决？

- 服务雪崩
- 服务降级
- 服务熔断
- 服务限流

### 流控模式

直接：

​	QPS，直接调用默认报出错误。

​	线程数？请求进入程序之后，防止线程占用过多

关联：B惹事，A挂了

链路：

### 流量效果

 快速失败、Warm Up、排队等待

预热：根据codeFactor（冷加载因子默认值3），从阈值/codeFactor，经过预热时长，才达到设置的QPS值（防止突然来的高流量把系统打死）

排队等待：匀速排队，让请求以均匀的速度通过，阈值类型必须为QPS，否则失效（排队匀速通过，设置超时时间）（漏桶算法）。这种方式主要用于处理间隔性突发的流量，例如消息队列，这样的场景在某一秒有大量的请求，下一秒又没有了

### 降级规则（熔断降级）

RT、异常比例、异常数

RT：平均响应时间，秒级。平均响应时间超出阈值且在时间窗口内通过的请求>=5，两个条件同时满足后触发降级

​		窗口期后关闭断路器

​		RT最大4900，更大通过参数设置

异常比例：QPS大于等于5且异常比例（秒级）超过阈值，触发降级；时间窗口结束后，关闭降级。

异常数：分钟级，异常数（分钟统计）超过阈值，触发降级；时间窗口期后，关闭降级（时间窗口期一定要大于60秒）

进一步说明：Sentinel的熔断是没有半开状态的.

### 热点Key（热点规则）

根据传入参数进行限流。

```java
@GetMapping("/testHotKey")
@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
public String testHotKey(@RequestParam(value = "p1'", required = false) String p1,
                         @RequestParam(value = "p2'", required = false) String p2) {
    return "test Hotkey";
}

public String deal_testHotKey(String p1, String p2, BlockException e) {
    return "-------deal_testHotKey";
}
```

必须配置兜底方法。

### 参数例外项

特例，当p1的值是5，访问阈值达到200。

总结，@SentinelResource主管配置出错，运行出错该走异常还走异常

### 系统规则（系统自适应限流）

系统自适应限流是从整体维度对应用入口流量就行控制。

### @SentinelResource

@HystrixCommand。

可自定义限流逻辑处理类。

注解不支持private

### 服务熔断

fallBack管理运行时异常（兜底方法）

blockHandler由sentinel监控

exceptionsToIgnore = {IllegalArgumentException.class}  如果报该异常，不再有fallback方法兜底，没有降级效果

### 整合OpenFeign

### 规则持久化

将限流规则持久化进Nacos保存，只要刷新8401某个rest地址，sentinel控制台的流控规则就能看到，只要nacos里面的配置不删除，针对8401上sentinel上的流控规则则持续有效。

```yml
server:
  port: 8401
spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        # nacos服务注册中心地址
        server-addr: localhost:8848
    sentinel:
      transport:
        # 配置dashboard地址
        dashboard: localhost:8080
        # 默认8710端口，假如被占用会自动从8719开始依次+1扫描，直至找到未被占用的端口
        port: 8719
      datasource: 
        dsl:
          nacos:
            server-addr: locolhost:8848
            dataId: cloudalibaba-sentinel-service
            groupId: DEFAULT_GROUP
            rule-type: flow

management:
  endpoints:
    web:
      exposure:
        include: '*'
```

nacos配置：

```json
{
  "resources": "/rateLimit/byUrl",  资源名称
  "limitApp": "default",						来源应用	
  "grade": 1,												阈值类型，0表示线程数，1表示QPS
  "count": 1,												单机阈值
  "strategy": 0,										流控模式，0表示直接，1表示关联，2表示链路
  "controlBehavior": 0,							流控效果，0表示快速失败，1表示Warm Up,2表示排队等待
  "clusterMode": false							是否集群
}
```

## Seata

分布式事务解决方案。全局数据一致性的问题。

分布式事务处理过程：

- 分布式事务处理过程的一个ID+三组件模型
  - id：全局事务id Translation ID XID
  - 三组件概念
    - Transaction Coordinator （TC） 事务协调器，维护全局事务和分支事务，驱动全局事务提交或回滚
    - Transaction Manager（TM） 定义全局事务范围：开始全局事务、提交或回滚全局事务
    - Resource Manager（RM） 控制分支事务，负责分支注册和报告分支事务状态，并驱动分支事务提交或回滚
- 处理过程

![image-20200903170943087](/Users/zoujidi/Library/Application Support/typora-user-images/image-20200903170943087.png)