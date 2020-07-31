package cn.zoujidi.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付实体类
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 15:21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    /**
     * id,主键
     */
    private Long id;

    /**
     * 流水号
     */
    private String serial;
}
