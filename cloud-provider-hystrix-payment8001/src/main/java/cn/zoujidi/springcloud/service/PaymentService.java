package cn.zoujidi.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * 业务类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月02日 21:35:00
 */
@Service
public class PaymentService {

    // ===============================服务降级======================================

    /**
     * Description: 正常访问，肯定OK
     * @param id
     * @return java.lang.String
     * @author ZouJiDi
     * @date 2020/8/2 9:39 下午
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "    paymentInfo_OK,id:    " + id + "\t" + "^_^";
    }

    /**
     * Description: 超时方法，触发降级
     * @return java.lang.String
     * @author ZouJiDi
     * @date 2020/8/2 9:39 下午
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        int timerNumber = 3;
        // int i = 1/0;
        try {
            TimeUnit.SECONDS.sleep(timerNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "    paymentInfo_TimeOut,id:    " + id + "\t" + "=_=" + "耗时" + timerNumber + "秒钟";
    }


    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "    8001系统繁忙或运行报错，请稍后再试,id:    " + id + "\t" + "┭┮﹏┭┮";
    }

    // =============================服务熔断=======================================

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            // 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            // 请求次数，，请求次数超过峰值，熔断器将会将会从关闭变为打开
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

}
