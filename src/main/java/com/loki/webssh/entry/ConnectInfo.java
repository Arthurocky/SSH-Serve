package com.loki.webssh.entry;

/**
 * 终端的连接信息
 *
 * @author Junpeng.Li
 * @date 2022-04-04 16:23:00
 */
public class ConnectInfo {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 主机IP
     */
    private String host;

    /**
     * 端口号
     */
    private int port = 22;

    /**
     * 超时时间, 单位: s(秒)
     */
    private int timeout = 3;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
