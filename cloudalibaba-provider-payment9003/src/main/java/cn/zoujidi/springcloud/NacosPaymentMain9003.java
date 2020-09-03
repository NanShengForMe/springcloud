package cn.zoujidi.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 主启动类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 14:58:00
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosPaymentMain9003 {

    public static void main(String[] args) {
            SpringApplication.run(NacosPaymentMain9003.class, args);
        }
}
