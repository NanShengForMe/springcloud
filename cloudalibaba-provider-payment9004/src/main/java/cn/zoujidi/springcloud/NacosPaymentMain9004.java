package cn.zoujidi.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 主启动类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 15:02:00
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NacosPaymentMain9004 {

    public static void main(String[] args) {
            SpringApplication.run(NacosPaymentMain9004.class, args);
        }
}
