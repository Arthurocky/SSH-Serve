package com.loki.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 *
 * @author Junpeng.Li
 * @date 2022-04-04 15:20:00
 */
@Controller
public class PageController {

    /**
     * 将默认请求指向index.html
     *
     * @return
     */
    @GetMapping(value = "/")
    public String path() {
        return "index";
    }

}
