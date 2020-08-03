package cn.zoujidi.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * 统一做服务降级处理
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月03日 14:33:00
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {

    @Override
    public String paymentInfo_OK(Integer id) {
        return "=========PaymentFallbackService  fallback   paymentInfo_OK";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "=========PaymentFallbackService  fallback   paymentInfo_Timeout";
    }
}
