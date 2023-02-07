package com.loki.webssh.entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loki.webssh.constant.MsgType;

/**
 * 页面数据
 *
 * @author Junpeng.Li
 * @date 2022-04-04 16:19:00
 */
public class PageData {

    /**
     * 消息类型
     *
     * {@link MsgType}
     */
    private MsgType type;

    /**
     * 消息。
     * <br>
     * 如果MsgType == connect, 这里的message类型就是{@link ConnectInfo}.
     * <br>
     * 如果MsgType == command, 这里的message类型就是{@link String}.
     */
    private Object message;

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getCommand() {
        if (MsgType.command.equals(this.type)) {
            return message.toString();
        }

        return null;
    }

    public ConnectInfo getConnectInfo() {
        if (MsgType.connect.equals(this.type)) {
            JSONObject json = (JSONObject) JSON.parse(this.message.toString());
            return JSON.toJavaObject(json, ConnectInfo.class);
        }

        return null;
    }
}
