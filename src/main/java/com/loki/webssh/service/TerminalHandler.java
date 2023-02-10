package com.loki.webssh.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.loki.webssh.entry.ConnectData;
import com.loki.webssh.entry.SSHEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 终端处理器
 *
 * @author Junpeng.Li
 * @date 2022-04-04 16:32:00
 */
@Component
public class TerminalHandler {

    private static final Logger logger = LoggerFactory.getLogger(TerminalHandler.class);

    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 连接终端
     *
     * @param info 连接信息
     */
    public void connect(SSHEntry entry, ConnectData info) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Session session;
                try {
                    session = entry.getjSch().getSession(info.getUsername(), info.getHost(), info.getPort());
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(info.getPassword());
                    // 连接, 并设置连接超时时间
                    session.connect(info.getTimeout() * 1000);

                    // 打开shell通道
                    Channel channel = session.openChannel("shell");
                    channel.connect(info.getTimeout() * 1000);

                    entry.setChannel(channel);
                } catch (Exception e) {
                    String errorMsg = String.format("Could not connect to '%s' (port %s): Connection failed.",
                            info.getHost(), info.getPort());

                    logger.error(errorMsg);

                    sendMessage(entry.getSession(), errorMsg.getBytes());
                    return;
                }

                sendCommand(entry.getChannel(), "\r");

                InputStream is = null;
                try {
                    is = entry.getChannel().getInputStream();

                    byte[] buffer = new byte[1024];
                    int i = 0;
                    while ((i = is.read(buffer)) != -1) {
                        sendMessage(entry.getSession(), Arrays.copyOfRange(buffer, 0, i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    entry.getChannel().disconnect();
                    session.disconnect();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 将命令发送给终端
     *
     * @param channel shell通道
     * @param command 命令
     */
    public void sendCommand(Channel channel, String command) {
        if (channel == null) {
            return;
        }

        try {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            logger.error("发送[ {} ]指令失败", command);
        }
    }

    /**
     * 发送消息给页面
     *
     * @param session WebSocket会话对象
     * @param message 要发送的信息
     */
    public void sendMessage(WebSocketSession session, byte[] message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logger.error("发送消息失败");
        }
    }
}
