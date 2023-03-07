package com.loki.webssh.socket;

import com.loki.webssh.constant.ConstantPool;
import com.loki.webssh.service.WebSSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * WebSocket的WebSocket消息处理器
 *
 * @author Arthurocky
 */
@Component
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Resource
    private WebSSHService webSshService;

    /**
     * 用户连接上之前的回调
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession)
    {
        logger.info("用户[ {} ]成功连接!", webSocketSession.getAttributes().get(ConstantPool.SESSION_KEY));

        // 调用初始化连接信息
        webSshService.init(webSocketSession);

    }

    /**
     * 收到WebSocket消息的回调<br>
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> message)
    {
        if (message instanceof TextMessage) {
            logger.info("用户[ {} ]发送命令: {}", webSocketSession.getAttributes().get(ConstantPool.SESSION_KEY), message.toString());
            // 调用service来接收消息，并处理接收到的终端命令
            webSshService.receiveHandle(webSocketSession, ((TextMessage) message).getPayload());
        }
        System.out.println("Unexpected WebSocket message type: " + message);
    }

    /**
     * 出现错误时的回调
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
    {
        logger.error("数据传输错误");
        logger.error("用户[ {} ]出现错误", session.getAttributes().get(ConstantPool.SESSION_KEY), exception);
    }

    /**
     * 断开连接后的回调
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
    {
        logger.info("用户[ {} ]已断开连接", session.getAttributes().get(ConstantPool.SESSION_KEY));
        //调用service关闭连接
        webSshService.close(session);
    }

    @Override
    public boolean supportsPartialMessages()
    {
        return false;
    }

}
