package com.starnetsecurity.starParkService.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
public class ChargeReceiveRecordPK implements Serializable {
    private String chargeReceiveId;
    private Timestamp chargeReceiveTime;

    @Column(name = "charge_receive_id")
    @Id
    public String getChargeReceiveId() {
        return chargeReceiveId;
    }

    public void setChargeReceiveId(String chargeReceiveId) {
        this.chargeReceiveId = chargeReceiveId;
    }

    @Column(name = "charge_receive_time")
    @Id
    public Timestamp getChargeReceiveTime() {
        return chargeReceiveTime;
    }

    public void setChargeReceiveTime(Timestamp chargeReceiveTime) {
        this.chargeReceiveTime = chargeReceiveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeReceiveRecordPK that = (ChargeReceiveRecordPK) o;

        if (chargeReceiveId != null ? !chargeReceiveId.equals(that.chargeReceiveId) : that.chargeReceiveId != null)
            return false;
        if (chargeReceiveTime != null ? !chargeReceiveTime.equals(that.chargeReceiveTime) : that.chargeReceiveTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargeReceiveId != null ? chargeReceiveId.hashCode() : 0;
        result = 31 * result + (chargeReceiveTime != null ? chargeReceiveTime.hashCode() : 0);
        return result;
    }
}
