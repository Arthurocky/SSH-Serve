package com.loki.webssh.entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loki.webssh.constant.MessageStyle;

/**
 * 设置页面数据
 */
public class PageData {

    /**
     * 消息类型
     * <p>
     * {@link MessageStyle}
     */
    private MessageStyle type;

    /**
     * 设置输入的信息的形式
     * <br>
     * 当 MessageStyle == connect --->message类型就是{@link ConnectData}.
     * <br>
     * 当 MessageStyle == command --->message类型就是{@link String}.
     */
    private Object message;

    public MessageStyle getType()
    {
        return type;
    }

    public void setType(MessageStyle type)
    {
        this.type = type;
    }

    public Object getMessage()
    {
        return message;
    }

    public void setMessage(Object message)
    {
        this.message = message;
    }

    public String getCommand()
    {
        if (MessageStyle.command.equals(this.type)) {
            return message.toString();
        }
        return null;
    }

    public ConnectData getConnectInfo()
    {
        if (MessageStyle.connect.equals(this.type)) {
            JSONObject json = (JSONObject) JSON.parse(this.message.toString());
            return JSON.toJavaObject(json, ConnectData.class);
        }
        return null;
    }
}
