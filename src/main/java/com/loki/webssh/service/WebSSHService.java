package com.loki.webssh.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSSH的业务逻辑<br>
 * 1.连接上终端（初始化连接）<br>
 * 2.服务端需要处理来自前端的消息（接收并处理前端消息）<br>
 * 3.将终端返回的消息回写到前端（数据回写前端）<br>
 * 4.关闭连接<br>
 * @author Arthurocky
 */
public interface WebSSHService {

    /**
     * 1.初始化WebSocket连接，连接上终端
     * @param session 获取WebSocket的会话对象
     */
    void init(WebSocketSession session);

    /**
     * 2.接收到页面发过来的命令<br>
     * 3并将终端返回的消息回写到前端
     *
     * @param session--> 与WebSocket会话的对象
     * @param message--> 执行的终端命令<br>
     *
     */
    void receiveHandle(WebSocketSession session, String message);

    /**
     * 4.关闭WebSockeet连接
     * @param session WebSocket的会话对象
     */
    void close(WebSocketSession session);


}
