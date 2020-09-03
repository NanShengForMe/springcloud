package cn.zoujidi.springcloud.Controller;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.hanlder.CustomerBlockHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月02日 22:39:00
 */
@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {
        return CommonResult.builder().message("按资源名称限流测试OK").code(200).data(new Payment(2020L, "serial001")).build();
    }

    public CommonResult handleException(BlockException ex) {
        return CommonResult.builder().code(444).message(ex.getClass().getCanonicalName() + "服务不可用").build();
    }

    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl() {
        return CommonResult.builder().message("按资Url限流测试OK").code(200).data(new Payment(2020L, "serial001")).build();
    }

    // CustomerBlockHandler

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2"
    )
    public CommonResult customerBlockHandler() {
        return CommonResult.builder().message("按资客户自定义限流测试OK").code(200).data(new Payment(2020L, "serial001")).build();
    }

}
