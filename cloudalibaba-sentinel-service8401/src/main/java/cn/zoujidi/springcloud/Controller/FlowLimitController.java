package cn.zoujidi.springcloud.Controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 流量控制
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月01日 22:45:00
 */
@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() + "\tB");
        return "------testB";
    }

    @GetMapping("/testD")
    public String testD() {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info(Thread.currentThread().getName() + "\tB 测试RT");

        int i = 10/0;
        log.info(Thread.currentThread().getName() + "\tB 测试异常比例");
        return "------testD";
    }

    @GetMapping("/testE")
    public String testE() {
        int i = 10/0;
        log.info(Thread.currentThread().getName() + "\tB 测试异常数");
        return "------testE";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1'", required = false) String p1,
                             @RequestParam(value = "p2'", required = false) String p2) {
        return "test Hotkey";
    }

    public String deal_testHotKey(String p1, String p2, BlockException e) {
        return "-------deal_testHotKey";
    }
}
