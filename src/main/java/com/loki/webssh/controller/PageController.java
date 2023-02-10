package com.loki.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面控制器
 *
 * @author Arthurocky
 */
@Controller
public class PageController {

    /**
     * 设置将默认请求指向位置
     */
    @RequestMapping(value = "/")
    public String path()
    {
        return "index";
    }

    @RequestMapping("WebSSH")
    public String path02()
    {
        return "index";
    }

    @RequestMapping("")
    public String path03()
    {
        return "index";
    }

}
