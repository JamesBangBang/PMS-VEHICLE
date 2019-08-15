package com.starnetsecurity.starParkService.entityEnum;

/**
 * Created by 宏炜 on 2017-08-21.
 */
public enum DBSyncEnum {
    UPDATE("更新"),
    INSERT("插入"),
    DELETE("删除");

    private String desc;

    DBSyncEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
