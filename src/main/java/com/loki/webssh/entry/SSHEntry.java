package com.loki.webssh.entry;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import org.springframework.web.socket.WebSocketSession;

/**
 * 存储SSH信息
 *
 * @author Junpeng.Li
 * @date 2022-04-04 15:53:00
 */
public class SSHEntry {

    /**
     * 用户的WebSocket会话信息
     */
    private WebSocketSession session;

    /**
     * JSch对象信息
     */
    private JSch jSch;

    /**
     * shell通道
     */
    private Channel channel;

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public JSch getjSch() {
        return jSch;
    }

    public void setjSch(JSch jSch) {
        this.jSch = jSch;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
