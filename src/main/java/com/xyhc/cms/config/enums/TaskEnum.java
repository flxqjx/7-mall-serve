package com.xyhc.cms.config.enums;

public enum TaskEnum {
    /**
     * 火警通知任务
     */
    FIRE_ALARM("火警通知定时任务");

    private String value;

    TaskEnum(String value) {
        this.value = value;
    }
}
