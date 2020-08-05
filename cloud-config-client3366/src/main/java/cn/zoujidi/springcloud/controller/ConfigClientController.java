package cn.zoujidi.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月05日 15:49:00
 */
@RestController
@Slf4j
@RefreshScope
public class ConfigClientController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return "serverPort:" + serverPort + "\t\n\n configInfo:" + configInfo;
    }
}
