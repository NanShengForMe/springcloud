package cn.zoujidi.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * 自定义负载均衡算法接口
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月02日 14:09:00
 */
public interface LoadBalancer {

    ServiceInstance instances(List<ServiceInstance> serviceInstances);

}
