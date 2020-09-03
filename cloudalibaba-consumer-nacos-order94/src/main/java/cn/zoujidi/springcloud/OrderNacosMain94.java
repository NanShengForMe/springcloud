package cn.zoujidi.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 主启动类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 15:17:00
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class OrderNacosMain94 {

    public static void main(String[] args) {
            SpringApplication.run(OrderNacosMain94.class, args);
        }
}
