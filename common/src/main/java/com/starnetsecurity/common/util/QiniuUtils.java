package com.starnetsecurity.common.util;


import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 宏炜 on 2017-08-16.
 */
public class QiniuUtils {

    private static final Logger log = LoggerFactory.getLogger(QiniuUtils.class);

    public static boolean upload(String aeskey,String secretKey,String bucketName,String filePath,String fileName) {
        try {
            Auth auth = Auth.create(aeskey, secretKey);
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            UploadManager uploadManager = new UploadManager(c);
            Response res = uploadManager.put(filePath, fileName, auth.uploadToken(bucketName));
            //log.info("七牛云上传图片返回信息：{}",res.toString());
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
        }catch (Exception e){
        }
        return false;
    }

    public static boolean upload(String aeskey,String secretKey,String bucketName,byte[] data,String fileName) {
        try {
            Auth auth = Auth.create(aeskey, secretKey);
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            UploadManager uploadManager = new UploadManager(c);
            Response res = uploadManager.put(data, fileName, auth.uploadToken(bucketName));
            //log.info("七牛云上传图片返回信息：{}",res.toString());
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
        }catch (Exception e){
        }
        return false;
    }

}
