package cn.zoujidi.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月31日 18:52:00
 */
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/consul")
    public String paymentconsul() {
        return "SpringBoot with consul :" + serverPort + "\t" + UUID.randomUUID().toString();
    }

}
