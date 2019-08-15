package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.nio.SemaphoreExecutor;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.QiniuUtils;
import com.starnetsecurity.parkClientServer.entity.UpImageFile;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.UpImageService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 宏炜 on 2017-08-17.
 */
@Service
public class UpImageServiceImpl implements UpImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpImageServiceImpl.class);

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public void updateUpload24HourImage() throws BizException {
        String hql = "from UpImageFile where createTime > ? and isUpload = ?";
        Timestamp time = new Timestamp(CommonUtils.getTimestamp().getTime() - (24 * 60 * 60 * 1000));
        Byte b = 0;
        List<UpImageFile> upImageFiles = (List<UpImageFile>)baseDao.queryForList(hql,time,b);

        if (CollectionUtils.isNotEmpty(upImageFiles)) {
            SemaphoreExecutor executor = new SemaphoreExecutor(upImageFiles.size(), "uploadImageThread");
            final CountDownLatch cdl = new CountDownLatch(upImageFiles.size());

            for (final UpImageFile upImageFile : upImageFiles) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean ret = false;
//                            boolean ret = QiniuUtils.upload(AppInfo.qiniuAesKey,AppInfo.qiniuSecretkey,AppInfo.qiniuBucketName,upImageFile.getFilePath(),upImageFile.getUpFileName());
                            if(ret){
                                upImageFile.setIsUpload((byte)1);
                                baseDao.update(upImageFile);
                            }
                        } catch (Exception ex) {
                            LOGGER.error("图片上传发生错误:{}",upImageFile.getFilePath(), ex);
                        } finally {
                            cdl.countDown();
                        }
                    }
                });
            }
            try {
                cdl.await();
            } catch (InterruptedException e) {
                LOGGER.error("图片上传发生错误 InterruptedException :", e);
            }
        }
    }
}
