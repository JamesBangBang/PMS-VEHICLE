package com.starnetsecurity.parkClientServer.allinpay;


public class QueryRsp extends BaseRsp {
	public String bizseq;
	public long amount;
	public String trxreserve;

	public String getTrxreserve() {
		return trxreserve;
	}
	public void setTrxreserve(String trxreserve) {
		this.trxreserve = trxreserve;
	}
	public String getBizseq() {
		return bizseq;
	}
	public void setBizseq(String bizseq) {
		this.bizseq = bizseq;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
}
