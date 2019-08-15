package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "discount_type", schema = "vams", catalog = "")
public class DiscountType {
    private String discountTypeId;
    private String discountTypeName;
    private String discountType;
    private String discountTypeDuration;
    private Float discountFee;
    private String discount;
    private String operationSource;
    private Timestamp addTime;
    private String discountCompanyName;
    private String discountInfo;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "discount_type_id")
    public String getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(String discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    @Basic
    @Column(name = "discount_type_name")
    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }

    @Basic
    @Column(name = "discount_type")
    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    @Basic
    @Column(name = "discount_type_duration")
    public String getDiscountTypeDuration() {
        return discountTypeDuration;
    }

    public void setDiscountTypeDuration(String discountTypeDuration) {
        this.discountTypeDuration = discountTypeDuration;
    }

    @Basic
    @Column(name = "discount_fee")
    public Float getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Float discountFee) {
        this.discountFee = discountFee;
    }

    @Basic
    @Column(name = "discount")
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    @Basic
    @Column(name = "discount_company_name")
    public String getDiscountCompanyName() {
        return discountCompanyName;
    }

    public void setDiscountCompanyName(String discountCompanyName) {
        this.discountCompanyName = discountCompanyName;
    }

    @Basic
    @Column(name = "discount_info")
    public String getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(String discountInfo) {
        this.discountInfo = discountInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountType that = (DiscountType) o;

        if (discountTypeId != null ? !discountTypeId.equals(that.discountTypeId) : that.discountTypeId != null)
            return false;
        if (discountTypeName != null ? !discountTypeName.equals(that.discountTypeName) : that.discountTypeName != null)
            return false;
        if (discountType != null ? !discountType.equals(that.discountType) : that.discountType != null) return false;
        if (discountTypeDuration != null ? !discountTypeDuration.equals(that.discountTypeDuration) : that.discountTypeDuration != null)
            return false;
        if (discountFee != null ? !discountFee.equals(that.discountFee) : that.discountFee != null) return false;
        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (discountCompanyName != null ? !discountCompanyName.equals(that.discountCompanyName) : that.discountCompanyName != null)
            return false;
        if (discountInfo != null ? !discountInfo.equals(that.discountInfo) : that.discountInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = discountTypeId != null ? discountTypeId.hashCode() : 0;
        result = 31 * result + (discountTypeName != null ? discountTypeName.hashCode() : 0);
        result = 31 * result + (discountType != null ? discountType.hashCode() : 0);
        result = 31 * result + (discountTypeDuration != null ? discountTypeDuration.hashCode() : 0);
        result = 31 * result + (discountFee != null ? discountFee.hashCode() : 0);
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (discountCompanyName != null ? discountCompanyName.hashCode() : 0);
        result = 31 * result + (discountInfo != null ? discountInfo.hashCode() : 0);
        return result;
    }
}
