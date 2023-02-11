package com.loki.webssh.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSSH的业务逻辑<br>
 * 1.连接上终端（初始化连接）<br>
 * 2.服务端需要处理来自前端的消息（接收并处理前端消息）<br>
 * 3.将终端返回的消息回写到前端（数据回写前端）<br>
 * 4.关闭连接<br>
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
