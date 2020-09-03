package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月03日 15:04:00
 */
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    public static Map<Long, Payment> hashMap = new HashMap<>();

    static {
        hashMap.put(1L, new Payment(1L, "asdfasdfasdfasdfadf"));
        hashMap.put(2L, new Payment(2L, "fdfdfdfdfddsdsdfdfd"));
        hashMap.put(3L, new Payment(3L, "ytytytytytytytytyty"));
    }

    @GetMapping("/paymentSql/{id}")
    public CommonResult<Payment> paymentSql(@PathVariable("id") Long id) {
        Payment payment = hashMap.get(id);
        CommonResult<Payment> commonResult = new CommonResult(200, "from mysql,serverPort:" + serverPort, payment);
        return commonResult;
    }
}
