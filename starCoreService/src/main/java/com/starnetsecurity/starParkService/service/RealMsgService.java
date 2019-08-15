package com.starnetsecurity.starParkService.service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-26.
 */
public interface RealMsgService {

    void pushInOutMsg(Map<String,Object> params) throws IOException;
}
