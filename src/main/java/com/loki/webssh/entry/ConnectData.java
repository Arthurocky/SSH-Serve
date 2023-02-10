package com.loki.webssh.entry;

/**
 * 终端连接信息
 *
 * @author Arthurocky
 */
public class ConnectData {
    /**
     * 操作
     */
    private String operate;
    /**
     * 命令
     */
    private String command = "";

    /**
     * 连接的IP地址
     */
    private String host;

    /**
     * 端口号默认为22
     */
    private int port = 22;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 设置5秒内完成连接，否则超时失败
     */
    private int timeout = 5;

    public ConnectData(String operate, String command, String host, int port, String username, String password, int timeout)
    {
        this.operate = operate;
        this.command = command;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }

    public ConnectData()
    {
    }

    public String getOperate()
    {
        return operate;
    }

    public void setOperate(String operate)
    {
        this.operate = operate;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
}
