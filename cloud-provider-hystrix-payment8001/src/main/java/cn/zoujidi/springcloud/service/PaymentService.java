package cn.zoujidi.springcloud.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 业务类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月02日 21:35:00
 */
@Service
public class PaymentService {

    /**
     * Description: 正常访问，肯定OK
     * @param id
     * @return java.lang.String
     * @author ZouJiDi
     * @date 2020/8/2 9:39 下午
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "    paymentInfo_OK,id:    " + id + "\t" + "^_^";
    }

    /**
     * Description: 超时方法，触发降级
     * @return java.lang.String
     * @author ZouJiDi
     * @date 2020/8/2 9:39 下午
     */
    public String paymentInfo_TimeOut(Integer id) {
        int timerNumber = 3;
        try {
            TimeUnit.SECONDS.sleep(timerNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "    paymentInfo_TimeOut,id:    " + id + "\t" + "=_=" + "耗时" + timerNumber + "秒钟";
    }
}
