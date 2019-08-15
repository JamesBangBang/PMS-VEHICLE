package sdk;

import com.starnetsecurity.common.util.QiniuUtils;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by JAMESBANG on 2018/5/24.
 */
public class CrawPicThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadPicThread.class);

    public String cameraIp;

    public String getCameraIp() {
        return cameraIp;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String fileName;


    @Override
    public void run(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://" + cameraIp + "/onvif/snapshot")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "0b12b0b5-55b1-13b2-13b5-f48b3200aa39")
                .build();
        try {
            Response response = client.newCall(request).execute();
            byte[] data = response.body().bytes();

            QiniuUtils.upload(AppInfo.qiniuAesKey,AppInfo.qiniuSecretkey,AppInfo.qiniuBucketName,data,this.fileName);
            response.body().close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
