package com.loki.webssh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSch;
import com.loki.webssh.constant.Const;
import com.loki.webssh.entry.PageData;
import com.loki.webssh.entry.SSHEntry;
import com.loki.webssh.service.SSHService;
import com.loki.webssh.service.TerminalHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现SSHService接口
 *
 * @author Junpeng.Li
 * @date 2022-04-04 15:50:00
 */
@Service
public class SSHServiceImpl implements SSHService {

    private static final Logger logger = LoggerFactory.getLogger(SSHServiceImpl.class);

    private Map<String, SSHEntry> connects = new ConcurrentHashMap<>();

    @Autowired
    private TerminalHandler terminalHandler;

    @Override
    public void init(WebSocketSession session) {
        JSch jSch = new JSch();

        SSHEntry entry = new SSHEntry() {{
            setSession(session);
            setjSch(jSch);
        }};

        connects.put(session.getAttributes().get(Const.SESSION_KEY).toString(), entry);
    }

    @Override
    public void close(WebSocketSession session) {
        String uuid = session.getAttributes().get(Const.SESSION_KEY).toString();
        SSHEntry entry = connects.get(uuid);
        if (entry != null && entry.getChannel() != null) {
            entry.getChannel().disconnect();
        }

        connects.remove(uuid);
    }

    @Override
    public void receive(WebSocketSession session, String message) {
        PageData data = JSON.toJavaObject((JSONObject) JSON.parse(message), PageData.class);

        String uuid = session.getAttributes().get(Const.SESSION_KEY).toString();
        SSHEntry entry = connects.get(uuid);
        if (entry == null) {
            logger.error("未找到用户[ {} ]的连接信息", uuid);
            return;
        }

        switch (data.getType()) {
            case connect:
                terminalHandler.connect(entry, data.getConnectInfo());
                break;
            case command:
                terminalHandler.sendCommand(entry.getChannel(), data.getCommand());
                break;
            default:
                logger.warn("不支持的消息类型");
                close(session);
        }
    }

}
