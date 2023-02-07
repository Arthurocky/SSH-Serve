package com.loki.webssh.constant;

/**
 * WebSocket传输过来消息的类型
 *
 * @author Junpeng.Li
 * @date 2022-04-04 16:19:00
 */
public enum MsgType {

    /**
     * 连接指令
     */
    connect(),

    /**
     * 终端命令
     */
    command()

}
