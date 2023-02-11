package com.loki.webssh.service;

import cn.hutool.core.date.DateUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.loki.webssh.constant.FileStyle;
import com.loki.webssh.entry.FileEntry;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * 使用jsch连接终端
 *
 * @author Arthurocky
 */
public class ConnectToSSH {

    public static void main(String[] args) throws Exception
    {
        JSch jSch = new JSch();
        //获取jsch的会话内容
        Session session = jSch.getSession("root", "192.168.124.54", 22);
        //设置密码
        session.setPassword("root");
        session.setConfig("StrictHostKeyChecking", "no");
        //超时时间10s
        session.connect(10 * 1000);

        //开启通道
        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        //通道连接 超时时间10s
        sftp.connect(10 * 1000);
        sftp.cd("/root");
        List<FileEntry> files = new ArrayList<>();
        Vector ls = sftp.ls(sftp.pwd());
        Enumeration elements = ls.elements();
        while (elements.hasMoreElements()) {
            ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) elements.nextElement();
            SftpATTRS attr = entry.getAttrs();
            if (entry.getFilename().startsWith(".")) {
                continue;
            }
            FileEntry f = new FileEntry() {{
                setPath(sftp.pwd());
                setName(entry.getFilename());
                if (attr.isDir()) {
                    setType(FileStyle.dir);
                } else if (attr.isReg()) {
                    setType(FileStyle.file);
                }
                setSize(attr.getSize());
                setCreateTime(DateUtil.parse(attr.getAtimeString()).toString());
                setUpdateTime(DateUtil.parse(attr.getMtimeString()).toString());
            }};
            files.add(f);
        }
        // sftp.put("E:\\MediaID.bin", "/root/sftp");
        sftp.get("/root/loki/config-loki", "D:\\");
        sftp.exit();
        session.disconnect();

        files.forEach(file -> {
            System.out.println(file.toString());
        });
    }
}
