/*
 *   @copyright      Copyright 2025 . [Jarvis] All rights reserved.
 *   @batchNo        907bce431df74a1cb559dc9f7c043858
 *   @table          AudioSession
 *   @template       entity v26
 *   重要提示：此处为代码水印，不要删除！
 */

package com.nova;


import java.util.Date;


/**
 * @author zouxiaoliang-jk@360shuke.com
 * @since 2025-05-19 13:42:39
 */

public class AudioSessionDTO {

    private static final long serialVersionUID = 6719080292985013081L;
    /**
     * 录音会话id
     */
    private String sessionId;
    /**
     * 员工编号
     */
    private String staffNo;
    /**
     * 客户编号
     */
    private String customerNo;
    /**
     * 开始录音时间
     */
    private Date startTime;
    /**
     * 结束录音时间
     */
    private Date endTime;
    /**
     * 录音文件，warehouse关联的文件编号
     */
    private String audioFile;
    /**
     * 录音文件大小
     */
    private Long fileSize;
    /**
     * 录音时长，毫秒
     */
    private Integer duration;
    /**
     * 录音状态
     */
    private String state;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

