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