package com.loki.webssh.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.loki.webssh.entry.ConnectData;
import com.loki.webssh.entry.SshConnectInfo;
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
 * 通过使用jsch连接并进行处理<br>
 * 实现数据回显
 *
 * @author Arthurocky
 */
@Component
public class TerminalHandler {

    private static final Logger logger = LoggerFactory.getLogger(TerminalHandler.class);

    /**
     * 创建线程池
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 连接终端
     */
    public void connect(SshConnectInfo connectInfo, ConnectData data)
    {
        //启动线程异步处理
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                Session session;
                try {
                    session = connectInfo.getjSch().getSession(data.getUsername(), data.getHost(), data.getPort());
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(data.getPassword());
                    // 连接, 并设置连接超时时间
                    session.connect(data.getTimeout() * 1000);
                    // 打开shell通道
                    Channel channel = session.openChannel("shell");
                    channel.connect(data.getTimeout() * 1000);
                    connectInfo.setChannel(channel);
                } catch (Exception e) {
                    logger.error("webssh连接异常");
                    logger.error("异常信息:{}", e.getMessage());
                    String errorMsg = String.format("Could not connect to '%s' (port %s): Connection failed.",
                            data.getHost(), data.getPort());
                    logger.error(errorMsg);
                    sendMessage(connectInfo.getSession(), errorMsg.getBytes());
                    return;
                }
                //转发消息
                sendCommand(connectInfo.getChannel(), "\r");
                InputStream is = null;
                try {
                    //读取终端返回的信息流
                    is = connectInfo.getChannel().getInputStream();
                    //循环读取
                    byte[] buffer = new byte[1024];
                    int i = 0;
                    //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                    while ((i = is.read(buffer)) != -1) {
                        sendMessage(connectInfo.getSession(), Arrays.copyOfRange(buffer, 0, i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //断开连接后关闭会话
                    connectInfo.getChannel().disconnect();
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
     * 发送命令给终端
     */
    public void sendCommand(Channel channel, String command)
    {
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
     */
    public void sendMessage(WebSocketSession session, byte[] message)
    {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logger.error("发送消息失败");
        }
    }
}
