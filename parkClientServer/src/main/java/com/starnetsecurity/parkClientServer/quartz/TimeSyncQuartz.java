package com.starnetsecurity.parkClientServer.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-09-08.
 */
@Component
public class TimeSyncQuartz {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSyncQuartz.class);

    public void run() {
        try {
            Integer successResult = 0;
            LOGGER.info("时间同步定时器启动");
            String responseStr = HttpRequestUtils.post("http://" + AppInfo.parkLotDomain + "/config/getTimeStamp",null);
            if(StringUtils.isBlank(responseStr)){
                throw new BizException("时间同步定时器，云服务器无信息返回");
            }
            JSONObject responseObject = JSON.parseObject(responseStr);
            Integer result = responseObject.getInteger("result");
            if(successResult.equals(result)){
                Long time = responseObject.getLong("data");
                Timestamp date = new Timestamp(time);
                String cmd = " cmd /c date " + CommonUtils.formatTimeStamp("yyyy-MM-dd",date); // 格式：yyyy-MM-dd
                Runtime.getRuntime().exec(cmd);

                cmd = " cmd /c time " + CommonUtils.formatTimeStamp("HH:mm:ss",date); // 格式 HH:mm:ss
                Runtime.getRuntime().exec(cmd);

                LOGGER.info("完成时间同步");
            }else{
                String msg = responseObject.getString("msg");
                throw new BizException("时间同步定时器，" + msg);
            }

        }catch (JSONException e){
            throw new BizException("时间同步定时器，JSON数据解析失败");
        }catch (BizException e){
            LOGGER.error(e.getMessage());
        }catch (Exception ex) {
            LOGGER.error("时间同步定时器错误:", ex);
        }
    }
}
