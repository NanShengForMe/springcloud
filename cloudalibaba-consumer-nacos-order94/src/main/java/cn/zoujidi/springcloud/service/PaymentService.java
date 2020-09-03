package cn.zoujidi.springcloud.service;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign接口
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 15:57:00
 */
//@Component("paymentService")
@FeignClient(value = "nacos-payment-provider", fallback = PaymentFallbackService.class)
public interface PaymentService {

    @GetMapping("/paymentSql/{id}")
    CommonResult<Payment> paymentSql(@PathVariable("id") Long id);

}
