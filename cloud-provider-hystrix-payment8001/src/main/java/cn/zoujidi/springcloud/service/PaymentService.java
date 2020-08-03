package cn.zoujidi.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

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

}
