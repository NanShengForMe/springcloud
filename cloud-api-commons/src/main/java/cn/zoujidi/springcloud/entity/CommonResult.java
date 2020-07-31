package cn.zoujidi.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回前端的json
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年07月29日 15:24:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResult<T> {

    /**
     * 404 not_found
     */

    private Integer code;

    private String message;

    private T data;


}
