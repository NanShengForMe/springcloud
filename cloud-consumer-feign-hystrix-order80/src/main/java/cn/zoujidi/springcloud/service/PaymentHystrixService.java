package cn.zoujidi.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 提供feign接口调用
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月03日 10:53:00
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-HYSTRIX-SERVICE", fallback = PaymentFallbackService.class)
public interface PaymentHystrixService {

    @GetMapping(value = "/payment/hystrix/ok/{id}", produces = "application/json;charset=UTF-8")
    String paymentInfo_OK(@PathVariable("id") Integer id);

    @GetMapping(value = "/payment/hystrix/timeout/{id}", produces = "application/json;charset=UTF-8")
    String paymentInfo_Timeout(@PathVariable("id") Integer id);
}
