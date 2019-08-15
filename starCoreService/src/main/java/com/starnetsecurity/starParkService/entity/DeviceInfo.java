package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "device_info", schema = "vamsserver", catalog = "")
public class DeviceInfo {
    private String deviceId;
    private String deviceName;
    private String deviceIp;
    private String devicePort;
    private String deviceUsername;
    private String devicePwd;
    private String deviceType;
    private String deviceChannelNo;
    private String parentDeviceId;
    private String ownCarRoad;
    private String outInFlag;
    private String relateIpcId;
    private String relateRoadGateId;
    private String content;
    private String way;
    private String voice;
    private String brightness;
    private String time;
    private String operationSource;
    private String rtspPort;
    private String deviceSubType;
    private String deviceSn;
    private Timestamp addTime;
    private String devStatus;
    private String uartDeviceAddr;
    private String uartBaudRate;
    private Integer uartDataBit;
    private Integer uartCrcBit;
    private Integer uartStopBit;
    private int uartFlowCtrl;
    private int uartProtocolType;
    private String subIpcId;
    private String relateTime;
    private Integer voiceChannel;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Basic
    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Basic
    @Column(name = "device_ip")
    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    @Basic
    @Column(name = "device_port")
    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    @Basic
    @Column(name = "device_username")
    public String getDeviceUsername() {
        return deviceUsername;
    }

    public void setDeviceUsername(String deviceUsername) {
        this.deviceUsername = deviceUsername;
    }

    @Basic
    @Column(name = "device_pwd")
    public String getDevicePwd() {
        return devicePwd;
    }

    public void setDevicePwd(String devicePwd) {
        this.devicePwd = devicePwd;
    }

    @Basic
    @Column(name = "device_type")
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Basic
    @Column(name = "device_channel_no")
    public String getDeviceChannelNo() {
        return deviceChannelNo;
    }

    public void setDeviceChannelNo(String deviceChannelNo) {
        this.deviceChannelNo = deviceChannelNo;
    }

    @Basic
    @Column(name = "parent_device_id")
    public String getParentDeviceId() {
        return parentDeviceId;
    }

    public void setParentDeviceId(String parentDeviceId) {
        this.parentDeviceId = parentDeviceId;
    }

    @Basic
    @Column(name = "own_car_road")
    public String getOwnCarRoad() {
        return ownCarRoad;
    }

    public void setOwnCarRoad(String ownCarRoad) {
        this.ownCarRoad = ownCarRoad;
    }

    @Basic
    @Column(name = "out_in_flag")
    public String getOutInFlag() {
        return outInFlag;
    }

    public void setOutInFlag(String outInFlag) {
        this.outInFlag = outInFlag;
    }

    @Basic
    @Column(name = "relate_ipc_id")
    public String getRelateIpcId() {
        return relateIpcId;
    }

    public void setRelateIpcId(String relateIpcId) {
        this.relateIpcId = relateIpcId;
    }

    @Basic
    @Column(name = "relate_road_gate_id")
    public String getRelateRoadGateId() {
        return relateRoadGateId;
    }

    public void setRelateRoadGateId(String relateRoadGateId) {
        this.relateRoadGateId = relateRoadGateId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "way")
    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    @Basic
    @Column(name = "voice")
    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Basic
    @Column(name = "brightness")
    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    @Basic
    @Column(name = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
    @Column(name = "rtsp_port")
    public String getRtspPort() {
        return rtspPort;
    }

    public void setRtspPort(String rtspPort) {
        this.rtspPort = rtspPort;
    }

    @Basic
    @Column(name = "device_sub_type")
    public String getDeviceSubType() {
        return deviceSubType;
    }

    public void setDeviceSubType(String deviceSubType) {
        this.deviceSubType = deviceSubType;
    }

    @Basic
    @Column(name = "device_sn")
    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "dev_status")
    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    @Basic
    @Column(name = "uart_device_addr")
    public String getUartDeviceAddr() {
        return uartDeviceAddr;
    }

    public void setUartDeviceAddr(String uartDeviceAddr) {
        this.uartDeviceAddr = uartDeviceAddr;
    }

    @Basic
    @Column(name = "uart_baud_rate")
    public String getUartBaudRate() {
        return uartBaudRate;
    }

    public void setUartBaudRate(String uartBaudRate) {
        this.uartBaudRate = uartBaudRate;
    }

    @Basic
    @Column(name = "uart_data_bit")
    public Integer getUartDataBit() {
        return uartDataBit;
    }

    public void setUartDataBit(Integer uartDataBit) {
        this.uartDataBit = uartDataBit;
    }

    @Basic
    @Column(name = "uart_crc_bit")
    public Integer getUartCrcBit() {
        return uartCrcBit;
    }

    public void setUartCrcBit(Integer uartCrcBit) {
        this.uartCrcBit = uartCrcBit;
    }

    @Basic
    @Column(name = "uart_stop_bit")
    public Integer getUartStopBit() {
        return uartStopBit;
    }

    public void setUartStopBit(Integer uartStopBit) {
        this.uartStopBit = uartStopBit;
    }

    @Basic
    @Column(name = "uart_flow_ctrl")
    public int getUartFlowCtrl() {
        return uartFlowCtrl;
    }

    public void setUartFlowCtrl(int uartFlowCtrl) {
        this.uartFlowCtrl = uartFlowCtrl;
    }

    @Basic
    @Column(name = "uart_protocol_type")
    public int getUartProtocolType() {
        return uartProtocolType;
    }

    public void setUartProtocolType(int uartProtocolType) {
        this.uartProtocolType = uartProtocolType;
    }

    @Basic
    @Column(name = "sub_ipc_id")
    public String getSubIpcId() {
        return subIpcId;
    }

    public void setSubIpcId(String subIpcId) {
        this.subIpcId = subIpcId;
    }

    @Basic
    @Column(name = "relate_time")
    public String getRelateTime() {
        return relateTime;
    }

    public void setRelateTime(String relateTime) {
        this.relateTime = relateTime;
    }

    @Basic
    @Column(name = "voice_channel")
    public Integer getVoiceChannel() {
        return voiceChannel;
    }

    public void setVoiceChannel(Integer voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Basic
    @Column(name = "department_id")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInfo that = (DeviceInfo) o;

        if (uartFlowCtrl != that.uartFlowCtrl) return false;
        if (uartProtocolType != that.uartProtocolType) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (deviceName != null ? !deviceName.equals(that.deviceName) : that.deviceName != null) return false;
        if (deviceIp != null ? !deviceIp.equals(that.deviceIp) : that.deviceIp != null) return false;
        if (devicePort != null ? !devicePort.equals(that.devicePort) : that.devicePort != null) return false;
        if (deviceUsername != null ? !deviceUsername.equals(that.deviceUsername) : that.deviceUsername != null)
            return false;
        if (devicePwd != null ? !devicePwd.equals(that.devicePwd) : that.devicePwd != null) return false;
        if (deviceType != null ? !deviceType.equals(that.deviceType) : that.deviceType != null) return false;
        if (deviceChannelNo != null ? !deviceChannelNo.equals(that.deviceChannelNo) : that.deviceChannelNo != null)
            return false;
        if (parentDeviceId != null ? !parentDeviceId.equals(that.parentDeviceId) : that.parentDeviceId != null)
            return false;
        if (ownCarRoad != null ? !ownCarRoad.equals(that.ownCarRoad) : that.ownCarRoad != null) return false;
        if (outInFlag != null ? !outInFlag.equals(that.outInFlag) : that.outInFlag != null) return false;
        if (relateIpcId != null ? !relateIpcId.equals(that.relateIpcId) : that.relateIpcId != null) return false;
        if (relateRoadGateId != null ? !relateRoadGateId.equals(that.relateRoadGateId) : that.relateRoadGateId != null)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (way != null ? !way.equals(that.way) : that.way != null) return false;
        if (voice != null ? !voice.equals(that.voice) : that.voice != null) return false;
        if (brightness != null ? !brightness.equals(that.brightness) : that.brightness != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (rtspPort != null ? !rtspPort.equals(that.rtspPort) : that.rtspPort != null) return false;
        if (deviceSubType != null ? !deviceSubType.equals(that.deviceSubType) : that.deviceSubType != null)
            return false;
        if (deviceSn != null ? !deviceSn.equals(that.deviceSn) : that.deviceSn != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (devStatus != null ? !devStatus.equals(that.devStatus) : that.devStatus != null) return false;
        if (uartDeviceAddr != null ? !uartDeviceAddr.equals(that.uartDeviceAddr) : that.uartDeviceAddr != null)
            return false;
        if (uartBaudRate != null ? !uartBaudRate.equals(that.uartBaudRate) : that.uartBaudRate != null) return false;
        if (uartDataBit != null ? !uartDataBit.equals(that.uartDataBit) : that.uartDataBit != null) return false;
        if (uartCrcBit != null ? !uartCrcBit.equals(that.uartCrcBit) : that.uartCrcBit != null) return false;
        if (uartStopBit != null ? !uartStopBit.equals(that.uartStopBit) : that.uartStopBit != null) return false;
        if (subIpcId != null ? !subIpcId.equals(that.subIpcId) : that.subIpcId != null) return false;
        if (relateTime != null ? !relateTime.equals(that.relateTime) : that.relateTime != null) return false;
        if (voiceChannel != null ? !voiceChannel.equals(that.voiceChannel) : that.voiceChannel != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (deviceIp != null ? deviceIp.hashCode() : 0);
        result = 31 * result + (devicePort != null ? devicePort.hashCode() : 0);
        result = 31 * result + (deviceUsername != null ? deviceUsername.hashCode() : 0);
        result = 31 * result + (devicePwd != null ? devicePwd.hashCode() : 0);
        result = 31 * result + (deviceType != null ? deviceType.hashCode() : 0);
        result = 31 * result + (deviceChannelNo != null ? deviceChannelNo.hashCode() : 0);
        result = 31 * result + (parentDeviceId != null ? parentDeviceId.hashCode() : 0);
        result = 31 * result + (ownCarRoad != null ? ownCarRoad.hashCode() : 0);
        result = 31 * result + (outInFlag != null ? outInFlag.hashCode() : 0);
        result = 31 * result + (relateIpcId != null ? relateIpcId.hashCode() : 0);
        result = 31 * result + (relateRoadGateId != null ? relateRoadGateId.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (way != null ? way.hashCode() : 0);
        result = 31 * result + (voice != null ? voice.hashCode() : 0);
        result = 31 * result + (brightness != null ? brightness.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (rtspPort != null ? rtspPort.hashCode() : 0);
        result = 31 * result + (deviceSubType != null ? deviceSubType.hashCode() : 0);
        result = 31 * result + (deviceSn != null ? deviceSn.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (devStatus != null ? devStatus.hashCode() : 0);
        result = 31 * result + (uartDeviceAddr != null ? uartDeviceAddr.hashCode() : 0);
        result = 31 * result + (uartBaudRate != null ? uartBaudRate.hashCode() : 0);
        result = 31 * result + (uartDataBit != null ? uartDataBit.hashCode() : 0);
        result = 31 * result + (uartCrcBit != null ? uartCrcBit.hashCode() : 0);
        result = 31 * result + (uartStopBit != null ? uartStopBit.hashCode() : 0);
        result = 31 * result + uartFlowCtrl;
        result = 31 * result + uartProtocolType;
        result = 31 * result + (subIpcId != null ? subIpcId.hashCode() : 0);
        result = 31 * result + (relateTime != null ? relateTime.hashCode() : 0);
        result = 31 * result + (voiceChannel != null ? voiceChannel.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
