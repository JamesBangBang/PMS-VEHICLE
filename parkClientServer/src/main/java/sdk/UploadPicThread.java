package sdk;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.common.util.QiniuUtils;
import com.starnetsecurity.common.util.Util;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.sockServer.ClientHeartThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by homew on 2018-04-24.
 */
public class UploadPicThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadPicThread.class);

    public String picPath;
    public String fileName;

    public String comid;
    public String orderId;
    public String parkOrderType;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getParkOrderType() {
        return parkOrderType;
    }

    public void setParkOrderType(String parkOrderType) {
        this.parkOrderType = parkOrderType;
    }

    @Override
    public void run() {
        /*String bolinkApiDomain = Constant.getPropertie("bolink.api.domain");
        String fileUrl =  "http://" + AppInfo.qiniuDomain + "/" + fileName;
        this.comid = Constant.getPropertie("bolink.park_id");
        byte[] data = null;
        int i = 30;
        while(i >= 0){
            try{
                data = Util.GetImageData(this.picPath);
                if(data.length > 1024){
                    break;
                }
            }catch (BizException e){
                LOGGER.error(e.getMessage());
            }catch (IOException e){
                LOGGER.error("图片上传IO异常，路径：{}",this.picPath,e);
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;

        }

        boolean ret = QiniuUtils.upload(AppInfo.qiniuAesKey,AppInfo.qiniuSecretkey,AppInfo.qiniuBucketName,data,this.fileName);
        if(ret){
            JSONObject params = new JSONObject();
            params.put("comid",comid);
            params.put("orderId",orderId);
            params.put("parkOrderType",parkOrderType);
            params.put("content",fileUrl);
            try {
                String res = HttpRequestUtils.postJson("http://" + bolinkApiDomain + "/upload/carpic.do",params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }
}
