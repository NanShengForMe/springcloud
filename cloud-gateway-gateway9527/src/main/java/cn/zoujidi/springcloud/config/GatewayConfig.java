package cn.zoujidi.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway配置
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月04日 15:45:00
 */
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
