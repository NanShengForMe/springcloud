package cn.zoujidi.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义负载均衡算法
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月02日 14:11:00
 */
@Component
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * Description: 获取当前请求是第几次请求（CAS+自旋锁）
     * @return int 当前是第几次请求
     * @author ZouJiDi
     * @date 2020/8/2 2:28 下午
     */
    public final int getAndIncrementt() {
        int current;
        int next;
        // CAS+自旋锁
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("****第几次访问，次数：next:" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        // 计算出访问服务位置下标
        int index = getAndIncrementt() % serviceInstances.size();
        return serviceInstances.get(index);
    }

}
