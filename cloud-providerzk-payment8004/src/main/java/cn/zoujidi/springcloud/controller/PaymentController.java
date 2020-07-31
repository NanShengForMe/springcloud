package cn.zoujidi.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月31日 15:10:00
 *
 *
 * zookeeper上注册的json串
 *
 *{
 *   "name": "cloud-provider-payment",
 *   "id": "61206be8-24e8-4b84-baa5-4e113b704aab",
 *   "address": "192.168.31.47",
 *   "port": 8004,
 *   "payload": {
 *     "@class": "org.springframework.cloud.zookeeper.discovery.ZookeeperInstance",
 *     "id": "application-1",
 *     "name": "cloud-provider-payment",
 *     "metadata": {}
 *   },
 *   "registrationTimeUTC": 1596182037985,
 *   "serviceType": "DYNAMIC",
 *   "uriSpec": {
 *     "parts": [
 *       {
 *         "value": "scheme",
 *         "variable": true
 *       },
 *       {
 *         "value": "://",
 *         "variable": false
 *       },
 *       {
 *         "value": "address",
 *         "variable": true
 *       },
 *       {
 *         "value": ":",
 *         "variable": false
 *       },
 *       {
 *         "value": "port",
 *         "variable": true
 *       }
 *     ]
 *   }
 * }
 *
 * Zookeeper采用临时节点，CAP中CP分支(保证数据一致性舍弃高可用)
 *
 */
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/zk")
    public String paymentzk() {
        return "SpringBoot with zookeeper :" + serverPort + "\t" + UUID.randomUUID().toString();
    }

}
