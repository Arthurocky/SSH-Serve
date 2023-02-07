package com.loki.webssh.socket;

import com.loki.webssh.constant.Const;
import com.loki.webssh.service.SSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket处理器
 *
 * @author Junpeng.Li
 * @date 2022-04-04 15:31:00
 */
@Component
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private SSHService sshService;

    /**
     * 用户连接之前的回调函数
     *
     * @param session WebSocket会话对象
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("用户[ {} ]成功连接!", session.getAttributes().get(Const.SESSION_KEY));

        // 初始化连接信息
        sshService.init(session);
    }

    /**
     * 收到WebSocket消息
     *
     * @param session WebSocket会话对象
     * @param message 接收到的消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            logger.info("用户[ {} ]发送命令: {}", session.getAttributes().get(Const.SESSION_KEY), message.toString());

            // 处理接收到的终端命令
            sshService.receive(session, ((TextMessage) message).getPayload());
        }
    }

    /**
     * 出现错误时的回调
     *
     * @param session   WebSocket会话对象
     * @param exception 错误信息
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("用户[ {} ]出现错误", session.getAttributes().get(Const.SESSION_KEY), exception);
    }

    /**
     * 断开连接后的回调
     *
     * @param session     WebSocket会话对象
     * @param closeStatus 关闭状态对象
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logger.info("用户[ {} ]已断开连接", session.getAttributes().get(Const.SESSION_KEY));

        // 关闭连接
        sshService.close(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
