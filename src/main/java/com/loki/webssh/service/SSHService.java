package com.loki.webssh.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * SSH接口
 *
 * @author Junpeng.Li
 * @date 2022-04-04 15:40:00
 */
public interface SSHService {

    /**
     * 初始化WebSocket连接
     *
     * @param session WebSocket会话对象
     */
    void init(WebSocketSession session);

    /**
     * 关闭WebSockeet连接
     *
     * @param session WebSocket会话对象
     */
    void close(WebSocketSession session);

    /**
     * 接收到页面发过来的命令
     *
     * @param session WebSocket会话对象
     * @param message 终端命令
     */
    void receive(WebSocketSession session, String message);
}
