package cn.zoujidi.springcloud.hanlder;

import cn.zoujidi.springcloud.entity.CommonResult;
import cn.zoujidi.springcloud.entity.Payment;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * 自定义处理类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年09月02日 22:56:00
 */

public class CustomerBlockHandler {

    public static CommonResult handlerException1(BlockException e) {
        return CommonResult.builder().message("按资客户自定义限流测试OK,global handlerException111").code(444).data(new Payment(2020L, "serial001")).build();
    }

    public static CommonResult handlerException2(BlockException e) {
        return CommonResult.builder().message("按资客户自定义限流测试OK,global handlerException222").code(444).data(new Payment(2020L, "serial001")).build();
    }

}
