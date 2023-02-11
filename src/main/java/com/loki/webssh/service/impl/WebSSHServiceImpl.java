package com.loki.webssh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSch;
import com.loki.webssh.constant.ConstantPool;
import com.loki.webssh.entry.PageData;
import com.loki.webssh.entry.SshConnectInfo;
import com.loki.webssh.service.TerminalHandler;
import com.loki.webssh.service.WebSSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现SSHService业务层的接口
 *
 * @author Arthurocky
 */
@Service
public class WebSSHServiceImpl implements WebSSHService {

    /**
     * 设置用于存放ssh连接信息，类型为map
     */
    private static final Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
    private Map<String, SshConnectInfo> connects = new ConcurrentHashMap<>();

    @Autowired
    private TerminalHandler terminalHandler;

    /**
     * 进行初始化连接<br>
     * 底层是依赖jsch实现的，所以这里是需要使用jsch去建立连接的
     */
    @Override
    public void init(WebSocketSession session)
    {
        JSch jSch = new JSch();
        SshConnectInfo sshConnectInfo = new SshConnectInfo() {
            {
                setjSch(jSch);
                setSession(session);
            }
        };
        //如果设置非公有，使用uuid代替常量池中的session_key
        //String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        String key = session.getAttributes().get(ConstantPool.SESSION_KEY).toString();
        //将夺取到的连接信息放入map中
        connects.put(key, sshConnectInfo);
    }


    /**
     * 处理客户端发送的数据
     */
    @Override
    public void receiveHandle(WebSocketSession session, String message)
    {
        PageData data = new PageData();
        data = JSON.toJavaObject((JSONObject) JSON.parse(message), PageData.class);
        String key = session.getAttributes().get(ConstantPool.SESSION_KEY).toString();
        SshConnectInfo ConnectInfo = connects.get(key);
        if (ConnectInfo == null) {
            logger.error("未找到用户[ {} ]的连接信息", key);
            return;
        }
        //获取客户端发来的数据类型，执行对应的操作
        switch (data.getType()) {
            //发来的是终端的用户名和密码等信息，执行终端的连接。
            case connect:
                terminalHandler.connect(ConnectInfo, data.getConnectInfo());
                break;
            //发来的是终端的操作命令，直接转发到终端并且获取终端的执行
            case command:
                terminalHandler.sendCommand(ConnectInfo.getChannel(), data.getCommand());
                break;
            default:
                logger.warn("不支持的该操作");
                close(session);
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void close(WebSocketSession session)
    {
        //公用设置
        //String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        String key = session.getAttributes().get(ConstantPool.SESSION_KEY).toString();
        SshConnectInfo sshConnectInfo = connects.get(key);
        if (sshConnectInfo != null && sshConnectInfo.getChannel() != null) {
            //断开连接
            sshConnectInfo.getChannel().disconnect();
        }
        //从map中移除
        connects.remove(key);
    }
}
