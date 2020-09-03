package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.service.PaymentService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 15:19:00
 */
@RestController
@Slf4j
public class CircleBreakerController {

    public static final String SERVER_URL = "http://nacos-payment-provider";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback", fallback = "handlerFallBack")
//    @SentinelResource(value = "fallback", blockHandler = "blockHandler")
    @SentinelResource(value = "fallback",
            blockHandler = "blockHandler",
            fallback = "handlerFallBack",
            exceptionsToIgnore = {IllegalArgumentException.class}
    )
    public CommonResult<Payment> fallBack(@PathVariable("id") Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVER_URL + "/paymentSql/" + id, CommonResult.class, id);
        if (id == 4) {
            throw new IllegalArgumentException("非法参数异常");
        } else if (result.getData() == null) {
            throw new NullPointerException("该id没有对应记录，空指针异常");
        }
        return result;
    }

    // 本例是fallback
    public CommonResult<Payment> handlerFallBack(@PathVariable("id") Long id, Throwable e) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<Payment>(444, "兜底异常处理方法ex：" + e.getMessage(), payment);
    }

    // 本例是blockHandler
    public CommonResult<Payment> blockHandler(@PathVariable("id") Long id, BlockException blockException) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<>(445, "blockHandler限流，无此流水：blockException" + blockException.getMessage(), payment);
    }

    // OpenFeign

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/consumer/paymentSql/{id}")
    public CommonResult<Payment> paymentSql(@PathVariable("id") Long id) {
        return paymentService.paymentSql(id);
    }
}
