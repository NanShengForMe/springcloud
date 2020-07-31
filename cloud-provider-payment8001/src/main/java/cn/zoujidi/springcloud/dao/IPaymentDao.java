package cn.zoujidi.springcloud.dao;

import cn.zoujidi.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 支付模块dao接口
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 15:58:00
 */
@Mapper
public interface IPaymentDao {

    /**
     * Description: 新增
     * @param payment 支付信息
     * @return int
     * @author ZouJiDi
     * @date 2020/7/29 3:59 下午
     */
    int addPayment(Payment payment);

    /**
     * Description: 通过id获取
     * @param id 主键
     * @return cn.zoujidi.springcloud.entity.Payment
     * @author ZouJiDi
     * @date 2020/7/29 4:00 下午
     */
    Payment getPaymentById(@Param("id") Long id);

}
