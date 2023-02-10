package com.loki.webssh.constant;

/**
 * WebSocket传输过来消息的类型
 */
public enum MessageStyle {
    /**
     * 实现连接指令
     */
    connect(),
    /**
     * 终端执行命令
     */
    command()
}
