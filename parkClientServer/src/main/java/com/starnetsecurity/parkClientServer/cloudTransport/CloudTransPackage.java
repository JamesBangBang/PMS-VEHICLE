package com.starnetsecurity.parkClientServer.cloudTransport;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by JAMESBANG on 2018/11/21.
 */
public class CloudTransPackage {
    private JSONObject uploadData;
    private CloudTransEnum cloudTransEnum;

    public JSONObject getUploadData() {
        return uploadData;
    }

    public void setUploadData(JSONObject uploadData) {
        this.uploadData = uploadData;
    }

    public CloudTransEnum getCloudTransEnum() {
        return cloudTransEnum;
    }

    public void setCloudTransEnum(CloudTransEnum cloudTransEnum) {
        this.cloudTransEnum = cloudTransEnum;
    }

}
