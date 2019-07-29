package com.jimo.mycost.data.dto;

/**
 * 文件展示实体项
 */
public class CloudFileEntry {
    private String name;
    /**
     * 单位为kb
     */
    private long size;

    private String modifyTime;

    public CloudFileEntry() {
    }

    public CloudFileEntry(String name, long size, String modifyTime) {
        this.name = name;
        this.size = size;
        this.modifyTime = modifyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
