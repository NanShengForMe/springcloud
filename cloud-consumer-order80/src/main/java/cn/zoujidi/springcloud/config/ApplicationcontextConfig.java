package cn.zoujidi.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 17:43:00
 */
@Configuration
public class ApplicationcontextConfig {

    @Bean
    // @LoadBalanced //使用LoadBalanced注解赋予了RestTemplate负载均衡的能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
