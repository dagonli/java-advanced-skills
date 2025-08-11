/*
 *   @copyright      Copyright 2025 . [Jarvis] All rights reserved.
 *   @batchNo        c911da99e98944f894b5478e8962f397
 *   @table          AudioTranscript
 *   @template       entity v26
 *   重要提示：此处为代码水印，不要删除！
 */

package com.nova;




import java.math.BigDecimal;


/**
 * 面谈内容转换(AudioTranscript)实体类
 *
 * @author zouxiaoliang-jk@360shuke.com
 * @since 2025-05-19 13:52:26
 */

public class AudioTranscriptCreateDTO {

    private static final long serialVersionUID = 5059540189387201685L;
    /**
     * 转换编号
     */
    private String transcriptNo;
    /**
     * 转换批次
     */
    private String transcriptBatch;
    /**
     * 录音会话id
     */
    private String sessionId;
    /**
     * 员工编号
     */
    private String staffNo;
    /**
     * 距离音频开始的毫秒偏移值
     */
    private Integer startTime;
    /**
     * 距离音频开始的毫秒偏移值
     */
    private Integer endTime;
    /**
     * 说话内容
     */
    private String content;
    /**
     * 分词
     */
    private String words;
    /**
     * 说话人，一般标识为0或1
     */
    private String speaker;
    /**
     * 可信度
     */
    private BigDecimal confidence;

    public String getTranscriptNo() {
        return transcriptNo;
    }

    public void setTranscriptNo(String transcriptNo) {
        this.transcriptNo = transcriptNo;
    }

    public String getTranscriptBatch() {
        return transcriptBatch;
    }

    public void setTranscriptBatch(String transcriptBatch) {
        this.transcriptBatch = transcriptBatch;
    }

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

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }
}

