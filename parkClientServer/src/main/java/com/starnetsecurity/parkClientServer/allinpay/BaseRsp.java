package com.starnetsecurity.parkClientServer.allinpay;
import com.starnetsecurity.parkClientServer.init.AppInfo;

import java.util.Date;


public class BaseRsp {
	public String appid;
	public String cusid;
	public String trxcode;
	public String timestamp;
	public String randomstr;
	public String sign;
	public String retcode;
	public String retmsg;

	
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getCusid() {
		return cusid;
	}
	public void setCusid(String cusid) {
		this.cusid = cusid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getRandomstr() {
		return randomstr;
	}
	public void setRandomstr(String randomstr) {
		this.randomstr = randomstr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getTrxcode() {
		return trxcode;
	}
	public void setTrxcode(String trxcode) {
		this.trxcode = trxcode;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void initHead(){
		cusid = AppInfo.allinpayBussinessId;
		appid = AppInfo.allinPayAppId;
		timestamp = FuncUtil.formatTime(new Date(), "yyyyMMddHHmmss");
		randomstr = FuncUtil.getRandcode(8);
		
	}
	
	public static BaseRsp getFaildResult(String msg){
		BaseRsp rsp = new BaseRsp();
		rsp.initHead();
		rsp.setRetcode("9999");
		rsp.setRetmsg(msg);
		return rsp;
	}
}
