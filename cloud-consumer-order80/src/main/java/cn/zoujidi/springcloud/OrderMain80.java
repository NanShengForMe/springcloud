package cn.zoujidi.springcloud;

import cn.zoujidi.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * 主启动类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 17:35:00
 */
@SpringBootApplication
@EnableEurekaClient
// @RibbonClient(name = "CLOUD-PROVIDER-SERVICE", configuration = MySelfRule.class)
public class OrderMain80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
}
