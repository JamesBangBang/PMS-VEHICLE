package com.starnetsecurity.starParkService.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 宏炜 on 2017-07-14.
 */
@Entity
public class Parks {
    private String id;
    private String parkName;
    private String wxConfigId;
    private String province;
    private String city;
    private String district;
    private String street;
    private BigDecimal lon;
    private BigDecimal lat;
    private Integer parkingLotNum;
    private Integer entranceNum;
    private Integer exitNum;
    private String hxUsername;
    private String hxPassword;
    private String clientId;
    private String passTime;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "park_name")
    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    @Basic
    @Column(name = "wx_config_id")
    public String getWxConfigId() {
        return wxConfigId;
    }

    public void setWxConfigId(String wxConfigId) {
        this.wxConfigId = wxConfigId;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "district")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Basic
    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "lon")
    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    @Basic
    @Column(name = "lat")
    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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
    @Column(name = "entrance_num")
    public Integer getEntranceNum() {
        return entranceNum;
    }

    public void setEntranceNum(Integer entranceNum) {
        this.entranceNum = entranceNum;
    }

    @Basic
    @Column(name = "exit_num")
    public Integer getExitNum() {
        return exitNum;
    }

    public void setExitNum(Integer exitNum) {
        this.exitNum = exitNum;
    }

    @Basic
    @Column(name = "hx_username")
    public String getHxUsername() {
        return hxUsername;
    }

    public void setHxUsername(String hxUsername) {
        this.hxUsername = hxUsername;
    }

    @Basic
    @Column(name = "hx_password")
    public String getHxPassword() {
        return hxPassword;
    }

    public void setHxPassword(String hxPassword) {
        this.hxPassword = hxPassword;
    }

    @Basic
    @Column(name = "client_id")
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Column(name = "pass_time")
    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parks parks = (Parks) o;

        if (id != null ? !id.equals(parks.id) : parks.id != null) return false;
        if (parkName != null ? !parkName.equals(parks.parkName) : parks.parkName != null) return false;
        if (wxConfigId != null ? !wxConfigId.equals(parks.wxConfigId) : parks.wxConfigId != null) return false;
        if (province != null ? !province.equals(parks.province) : parks.province != null) return false;
        if (city != null ? !city.equals(parks.city) : parks.city != null) return false;
        if (district != null ? !district.equals(parks.district) : parks.district != null) return false;
        if (street != null ? !street.equals(parks.street) : parks.street != null) return false;
        if (lon != null ? !lon.equals(parks.lon) : parks.lon != null) return false;
        if (lat != null ? !lat.equals(parks.lat) : parks.lat != null) return false;
        if (parkingLotNum != null ? !parkingLotNum.equals(parks.parkingLotNum) : parks.parkingLotNum != null)
            return false;
        if (entranceNum != null ? !entranceNum.equals(parks.entranceNum) : parks.entranceNum != null) return false;
        if (exitNum != null ? !exitNum.equals(parks.exitNum) : parks.exitNum != null) return false;
        if (hxUsername != null ? !hxUsername.equals(parks.hxUsername) : parks.hxUsername != null) return false;
        if (hxPassword != null ? !hxPassword.equals(parks.hxPassword) : parks.hxPassword != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parkName != null ? parkName.hashCode() : 0);
        result = 31 * result + (wxConfigId != null ? wxConfigId.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (parkingLotNum != null ? parkingLotNum.hashCode() : 0);
        result = 31 * result + (entranceNum != null ? entranceNum.hashCode() : 0);
        result = 31 * result + (exitNum != null ? exitNum.hashCode() : 0);
        result = 31 * result + (hxUsername != null ? hxUsername.hashCode() : 0);
        result = 31 * result + (hxPassword != null ? hxPassword.hashCode() : 0);
        return result;
    }
}
