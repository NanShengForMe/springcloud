package cn.zoujidi.springcloud.controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private DiscoveryClient discoveryClient;

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

    @GetMapping(value = "/payment/discovery", produces = {"application/json;charset=UTF-8"})
    public Object discovery() {
        // 获取所有服务类型
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("===========service:" + service);
        }

        // 获取某个服务下有多少实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }

        return discoveryClient;
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    /**
     * Description: 模拟长流程处理（测试OpenFeign超时设置）
     * @return java.lang.String
     * @author ZouJiDi
     * @date 2020/8/2 3:59 下午
     */
    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout() {
        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

}
