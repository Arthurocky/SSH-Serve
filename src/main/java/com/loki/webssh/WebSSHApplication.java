package com.loki.webssh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * WebSSH的启动入口类
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class WebSSHApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSSHApplication.class, args);
    }
}
