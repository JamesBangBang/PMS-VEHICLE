package com.starnetsecurity.parkClientServer.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by 宏炜 on 2018-01-21.
 */
public interface InOutMsgService {

    void carInMsgPush(String parkId, String carNo, String time,String port) throws IOException;

    void carOutMsgPush(String parkId, String carNo, String time, String port, BigDecimal fee) throws IOException;

    void pushInOutMsg(Map<String,Object> params) throws IOException;
}
