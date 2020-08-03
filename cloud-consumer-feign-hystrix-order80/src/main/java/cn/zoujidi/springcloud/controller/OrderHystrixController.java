package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月03日 10:56:00
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Autowired
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}", produces = "application/json;charset=UTF-8")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}", produces = "application/json;charset=UTF-8")
//    @HystrixCommand(fallbackMethod = "paymentTimeOutFallBackMethod", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
//    })
    @HystrixCommand
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        int i = 1/0;
        return paymentHystrixService.paymentInfo_Timeout(id);
    }

    public String paymentTimeOutFallBackMethod(Integer id) {
        return "我是消费者80，对方支付系统繁忙请10秒之后再试试或者自己运行出错，请检查自己，┭┮﹏┭┮" + id;
    }

    // 下面是全局fallback方法
    public String payment_Global_FallbackMethod() {
        return "Global异常处理信息，请稍后再试。┭┮﹏┭┮";
    }
}
