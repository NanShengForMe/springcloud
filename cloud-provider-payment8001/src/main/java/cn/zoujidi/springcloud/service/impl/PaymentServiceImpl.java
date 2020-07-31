package cn.zoujidi.springcloud.service.impl;

import cn.zoujidi.springcloud.dao.IPaymentDao;
import cn.zoujidi.springcloud.entity.Payment;
import cn.zoujidi.springcloud.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付服务
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 16:14:00
 */
@Service("paymentService")
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentDao paymentDao;

    @Override
    public int addPayment(Payment payment) {
        return paymentDao.addPayment(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
