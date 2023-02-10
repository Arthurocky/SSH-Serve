package com.loki.webssh.entry;

import cn.hutool.core.io.FileUtil;
import com.loki.webssh.constant.FileStyle;

/**
 * 设置文件基本信息
 *
 * @author Arthurocky
 */
public class FileEntry {

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件类型
     */
    private FileStyle type;

    /**
     * 文件名
     */
    private String name;

    private Long size;

    private String sizeStr;

    public FileEntry(String createTime, String updateTime, String path, FileStyle type, String name, Long size, String sizeStr)
    {
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.path = path;
        this.type = type;
        this.name = name;
        this.size = size;
        this.sizeStr = sizeStr;
    }

    public FileEntry()
    {
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public FileStyle getType()
    {
        return type;
    }

    public void setType(FileStyle type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize(Long size)
    {
        this.size = size;

        this.setSizeStr(FileUtil.readableFileSize(size));
    }

    public String getSizeStr()
    {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr)
    {
        this.sizeStr = sizeStr;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        return "FileEntry{" +
                "createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", sizeStr='" + sizeStr + '\'' +
                '}';
    }
}
