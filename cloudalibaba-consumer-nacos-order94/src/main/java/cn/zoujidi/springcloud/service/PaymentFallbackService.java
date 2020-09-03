package cn.zoujidi.springcloud.service;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 16:00:00
 */
@Component
public class PaymentFallbackService implements PaymentService {

    @Override
    public CommonResult<Payment> paymentSql(Long id) {
        return new CommonResult<>(444, "服务降级返回，PaymentFallbackService", new Payment(id, "error"));
    }

}
