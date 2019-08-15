package sdk;

/**
 * Created by 瀹忕倻 on 2017-11-17.
 */
public enum BolinkBizEnum {

    query_price("查询订单价格"),
    prepay_order("预付订单通知"),
    monthcard_pay("月卡续费"),
    nolicence_in_park("无牌车入场请求"),
    outpark("电子收费异步返回结果"),
    query_prodprice("查询月卡价格");

    private String desc;
    BolinkBizEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
