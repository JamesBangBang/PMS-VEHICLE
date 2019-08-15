package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "led_display_config", schema = "vams", catalog = "")
public class LedDisplayConfig {
    private String id;
    private String devId;
    private Integer sceneNo;
    private String scene;
    private String topRowContent;
    private String middleRowContent;
    private String buttomRowContent;
    private String fourthRowContent;
    private String voiceBroadcast;
    private Integer voiceBroadcastVolume;
    private String displayMode;
    private String movingDirection;
    private String movementSpeed;
    private String operationSource;
    private Timestamp addTime;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "dev_id")
    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    @Basic
    @Column(name = "scene_no")
    public Integer getSceneNo() {
        return sceneNo;
    }

    public void setSceneNo(Integer sceneNo) {
        this.sceneNo = sceneNo;
    }

    @Basic
    @Column(name = "scene")
    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    @Basic
    @Column(name = "top_row_content")
    public String getTopRowContent() {
        return topRowContent;
    }

    public void setTopRowContent(String topRowContent) {
        this.topRowContent = topRowContent;
    }

    @Basic
    @Column(name = "middle_row_content")
    public String getMiddleRowContent() {
        return middleRowContent;
    }

    public void setMiddleRowContent(String middleRowContent) {
        this.middleRowContent = middleRowContent;
    }

    @Basic
    @Column(name = "buttom_row_content")
    public String getButtomRowContent() {
        return buttomRowContent;
    }

    public void setButtomRowContent(String buttomRowContent) {
        this.buttomRowContent = buttomRowContent;
    }

    @Basic
    @Column(name = "fourth_row_content")
    public String getFourthRowContent() {
        return fourthRowContent;
    }

    public void setFourthRowContent(String fourthRowContent) {
        this.fourthRowContent = fourthRowContent;
    }

    @Basic
    @Column(name = "voice_broadcast")
    public String getVoiceBroadcast() {
        return voiceBroadcast;
    }

    public void setVoiceBroadcast(String voiceBroadcast) {
        this.voiceBroadcast = voiceBroadcast;
    }

    @Basic
    @Column(name = "voice_broadcast_volume")
    public Integer getVoiceBroadcastVolume() {
        return voiceBroadcastVolume;
    }

    public void setVoiceBroadcastVolume(Integer voiceBroadcastVolume) {
        this.voiceBroadcastVolume = voiceBroadcastVolume;
    }

    @Basic
    @Column(name = "display_mode")
    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    @Basic
    @Column(name = "moving_direction")
    public String getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(String movingDirection) {
        this.movingDirection = movingDirection;
    }

    @Basic
    @Column(name = "movement_speed")
    public String getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(String movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    @Basic
    @Column(name = "operation_source")
    public String getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(String operationSource) {
        this.operationSource = operationSource;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LedDisplayConfig that = (LedDisplayConfig) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (devId != null ? !devId.equals(that.devId) : that.devId != null) return false;
        if (sceneNo != null ? !sceneNo.equals(that.sceneNo) : that.sceneNo != null) return false;
        if (scene != null ? !scene.equals(that.scene) : that.scene != null) return false;
        if (topRowContent != null ? !topRowContent.equals(that.topRowContent) : that.topRowContent != null)
            return false;
        if (middleRowContent != null ? !middleRowContent.equals(that.middleRowContent) : that.middleRowContent != null)
            return false;
        if (buttomRowContent != null ? !buttomRowContent.equals(that.buttomRowContent) : that.buttomRowContent != null)
            return false;
        if (fourthRowContent != null ? !fourthRowContent.equals(that.fourthRowContent) : that.fourthRowContent != null)
            return false;
        if (voiceBroadcast != null ? !voiceBroadcast.equals(that.voiceBroadcast) : that.voiceBroadcast != null)
            return false;
        if (voiceBroadcastVolume != null ? !voiceBroadcastVolume.equals(that.voiceBroadcastVolume) : that.voiceBroadcastVolume != null)
            return false;
        if (displayMode != null ? !displayMode.equals(that.displayMode) : that.displayMode != null) return false;
        if (movingDirection != null ? !movingDirection.equals(that.movingDirection) : that.movingDirection != null)
            return false;
        if (movementSpeed != null ? !movementSpeed.equals(that.movementSpeed) : that.movementSpeed != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (devId != null ? devId.hashCode() : 0);
        result = 31 * result + (sceneNo != null ? sceneNo.hashCode() : 0);
        result = 31 * result + (scene != null ? scene.hashCode() : 0);
        result = 31 * result + (topRowContent != null ? topRowContent.hashCode() : 0);
        result = 31 * result + (middleRowContent != null ? middleRowContent.hashCode() : 0);
        result = 31 * result + (buttomRowContent != null ? buttomRowContent.hashCode() : 0);
        result = 31 * result + (fourthRowContent != null ? fourthRowContent.hashCode() : 0);
        result = 31 * result + (voiceBroadcast != null ? voiceBroadcast.hashCode() : 0);
        result = 31 * result + (voiceBroadcastVolume != null ? voiceBroadcastVolume.hashCode() : 0);
        result = 31 * result + (displayMode != null ? displayMode.hashCode() : 0);
        result = 31 * result + (movingDirection != null ? movingDirection.hashCode() : 0);
        result = 31 * result + (movementSpeed != null ? movementSpeed.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
}
