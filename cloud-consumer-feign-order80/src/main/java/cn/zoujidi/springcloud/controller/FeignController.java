package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.service.PaymentFeignService;
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
 * @date 2020年08月02日 15:40:00
 */
@RestController
@Slf4j
public class FeignController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping(value = "/consumer/payment/feign/timeout", produces = "application/json;charset=UTF-8")
    public String paymentFeignTimeout() {
        // OpenFeign-Ribbon，客户端一般默认等待1秒钟
        return paymentFeignService.paymentFeignTimeout();
    }

}
