package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * PaymentController
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 16:18:00
 */
@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/payment")
    public CommonResult addPayment(@RequestBody Payment payment) {
        int result = paymentService.addPayment(payment);
        log.info("插入结果:......." + result);
        if (result > 0) {
            return CommonResult.builder().code(200).message("插入数据库成功!serverPort:"+serverPort).data(payment).build();
        }
        return CommonResult.builder().code(444).message("插入数据库失败").build();
    }

    @GetMapping(value = "/payment/{id}", produces = {"application/json;charset=UTF-8"})
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("查询结果:......" + payment);
        if (payment != null) {
            return CommonResult.builder().code(200).message("查询成功!serverPort:"+serverPort).data(payment).build();
        }
        return CommonResult.builder().code(444).message("没有对应记录，查询id：" + id).build();
    }


}
