package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "carpark_register", schema = "vams", catalog = "")
public class CarparkRegister {
    private String id;
    private String carparkName;
    private Integer parkingLotNum;
    private String serviceId;
    private String userName;
    private String password;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "carpark_name")
    public String getCarparkName() {
        return carparkName;
    }

    public void setCarparkName(String carparkName) {
        this.carparkName = carparkName;
    }

    @Basic
    @Column(name = "parking_lot_num")
    public Integer getParkingLotNum() {
        return parkingLotNum;
    }

    public void setParkingLotNum(Integer parkingLotNum) {
        this.parkingLotNum = parkingLotNum;
    }

    @Basic
    @Column(name = "service_id")
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarparkRegister that = (CarparkRegister) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (parkingLotNum != null ? !parkingLotNum.equals(that.parkingLotNum) : that.parkingLotNum != null)
            return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (parkingLotNum != null ? parkingLotNum.hashCode() : 0);
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
