package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.commonService.util.DeviceLedFourThread;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JAMESBANG on 2019/3/2.
 */
public class GuideScreenThread extends Thread{
    private static String control = "";

    private static Logger LOGGER = LoggerFactory.getLogger(GuideScreenThread.class);

    private static int aucCRCHi[] = {
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40
    };

    private static int aucCRCLo[] = {
            0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07, 0xC7,
            0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E,
            0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09, 0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9,
            0x1B, 0xDB, 0xDA, 0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC,
            0x14, 0xD4, 0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
            0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32,
            0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 0x3C, 0xFC, 0xFD, 0x3D,
            0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38,
            0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF,
            0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
            0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60, 0x61, 0xA1,
            0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67, 0xA5, 0x65, 0x64, 0xA4,
            0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB,
            0x69, 0xA9, 0xA8, 0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA,
            0xBE, 0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
            0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0,
            0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97,
            0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E,
            0x5A, 0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89,
            0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
            0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42, 0x43, 0x83,
            0x41, 0x81, 0x80, 0x40
    };

    private static boolean suspend = false;

    public boolean isSuspend() {
        return suspend;
    }

    public static void setSuspend(boolean suspendTemp) {
        if (!suspendTemp){
            synchronized (control){
                control.notifyAll();
            }
        }
        suspend = suspendTemp;
    }

    private static List<JSONObject> playList;

    public static void addPlayListInfo(JSONObject playInfo){
        //是否需要新增记录
        boolean isNeedAdd = true;

        String inOutType = playInfo.getString("inOutType");
        String carparkId = playInfo.getString("carparkId");

        if (playList.size() > 0){
            if (inOutType.equals("0")){
                for (int i = 0; i < playList.size(); i++) {
                    String deviceIdBefore = playList.get(i).getString("deviceId");
                    String deviceId = playInfo.getString("deviceId");
                    if (deviceIdBefore.equals(deviceId)) {
                        //将第i个元素替换
                        playList.set(i, playInfo);
                        isNeedAdd = false;
                    }
                }
            }else {
                for (int i = 0; i < playList.size(); i++) {
                    //出场的话全部都不添加记录
                    isNeedAdd = false;
                    String carparkIdBefore = playList.get(i).getString("carparkId");
                    if (carparkId.equals(carparkIdBefore)) {
                        //将第i个元素替换
                        playInfo.put("inOutType",inOutType);
                        playInfo.put("deviceId",playList.get(i).getString("deviceId"));
                        playInfo.put("deviceIp",playList.get(i).getString("deviceIp"));
                        playInfo.put("devicePort",playList.get(i).getString("devicePort"));
                        playInfo.put("deviceUserName",playList.get(i).getString("deviceUserName"));
                        playInfo.put("devicePwd",playList.get(i).getString("devicePwd"));
                        playList.set(i, playInfo);
                    }
                }
            }


        }else {
            if (inOutType.equals("1")){
                isNeedAdd = false;
            }
        }

        if (isNeedAdd) {
            playList.add(playInfo);
        }
    }

    private static void initServer(){
        playList = new ArrayList<>();
    }


    @Override
    public void run(){
        initServer();
        while (true){
            synchronized (control){
                if (!suspend){
                    if (playList.size() > 0) {
                        try {
                            Date nowTime = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");
                            String hhss = simpleDateFormat.format(nowTime);
                            boolean isInteger = false;
                            if (("0900".equals(hhss)) || ("0901".equals(hhss))) {
                                isInteger = true;
                            }

                            for (int i = 0; i < playList.size(); i++) {
                                JSONObject needPlayInfo = playList.get(i);

                                String deviceIp = needPlayInfo.getString("deviceIp");
                                String devicePort = needPlayInfo.getString("devicePort");
                                String deviceUserName = needPlayInfo.getString("deviceUserName");
                                String devicePwd = needPlayInfo.getString("devicePwd");
                                Integer availableNum = Integer.valueOf(needPlayInfo.get("availableNum") + "");
                                if (availableNum > 9999){
                                    availableNum = 9999;
                                }
                                Integer thousandStr = availableNum / 1000;
                                Integer hundredStr = (availableNum - thousandStr * 1000) / 100;
                                Integer tenStr = (availableNum - thousandStr * 1000 - hundredStr * 100) / 10;
                                Integer singleStr = availableNum - thousandStr * 1000 - hundredStr * 100 - tenStr * 10;
                                String playStr = "C9020008" + thousandStr + hundredStr + tenStr + singleStr;

                                long firstStr = Long.parseLong("C9",16);
                                long secondStr = Long.parseLong("02",16);
                                long thirdStr = Long.parseLong("00",16);
                                long fourthStr = Long.parseLong("08",16);
                                long fifthStr = Long.parseLong(thousandStr + "" + hundredStr,16);
                                long sixthStr = Long.parseLong(tenStr + "" + singleStr,16);
                                String checkStr = (Long.toHexString(firstStr ^ secondStr ^ thirdStr ^ fourthStr ^ fifthStr ^ sixthStr)).toUpperCase();
                                playStr = playStr + checkStr + "9C";
                                LOGGER.info("下发剩余车位：" + availableNum + " 进出类型为：" + needPlayInfo.getInteger("inOutType"));
                                List<JSONObject> ledParamsList = new ArrayList<>();
                                JSONObject params = new JSONObject();
                                params.put("uartID", AppInfo.guideScreenPos);
                                params.put("write_data", getLedPlayStrToBase(playStr));
                                params.put("need_rspdata", 0);
                                params.put("action","set");
                                ledParamsList.add(params);

                                DeviceLedFourThread deviceLedFourThread = new DeviceLedFourThread();
                                deviceLedFourThread.setParamsList(ledParamsList);
                                deviceLedFourThread.setUsername(deviceUserName);
                                deviceLedFourThread.setPassword(devicePwd);
                                deviceLedFourThread.setIp(deviceIp);
                                deviceLedFourThread.setPort(Integer.parseInt(devicePort));
                                deviceLedFourThread.start();

                                if (isInteger){
                                    //整点更新总车位
                                    Integer totalNum = Integer.valueOf(needPlayInfo.get("totalNum") + "");
                                    if (totalNum > 9999){
                                        totalNum = 9999;
                                    }
                                    thousandStr = totalNum / 1000;
                                    hundredStr = (totalNum - thousandStr * 1000) / 100;
                                    tenStr = (totalNum - thousandStr * 1000 - hundredStr * 100) / 10;
                                    singleStr = totalNum - thousandStr * 1000 - hundredStr * 100 - tenStr * 10;
                                    playStr = "C9010008" + thousandStr + hundredStr + tenStr + singleStr;

                                    firstStr = Long.parseLong("C9",16);
                                    secondStr = Long.parseLong("01",16);
                                    thirdStr = Long.parseLong("00",16);
                                    fourthStr = Long.parseLong("08",16);
                                    fifthStr = Long.parseLong(thousandStr + "" + hundredStr,16);
                                    sixthStr = Long.parseLong(tenStr + "" + singleStr,16);
                                    checkStr = (Long.toHexString(firstStr ^ secondStr ^ thirdStr ^ fourthStr ^ fifthStr ^ sixthStr)).toUpperCase();
                                    playStr = playStr + checkStr + "9C";

                                    List<JSONObject> screenParamsList = new ArrayList<>();
                                    JSONObject paramTotal = new JSONObject();
                                    paramTotal.put("uartID", AppInfo.guideScreenPos);
                                    paramTotal.put("write_data", getLedPlayStrToBase(playStr));
                                    paramTotal.put("need_rspdata", 0);
                                    paramTotal.put("action","set");
                                    screenParamsList.add(paramTotal);

                                    DeviceLedFourThread deviceLedFourThreadTotal = new DeviceLedFourThread();
                                    deviceLedFourThreadTotal.setParamsList(screenParamsList);
                                    deviceLedFourThreadTotal.setUsername(deviceUserName);
                                    deviceLedFourThreadTotal.setPassword(devicePwd);
                                    deviceLedFourThreadTotal.setIp(deviceIp);
                                    deviceLedFourThreadTotal.setPort(Integer.valueOf(devicePort));
                                    deviceLedFourThreadTotal.start();
                                }
                            }
                            setSuspend(true);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private static String getLedPlayStrToBase(String playStr){
        byte[] data = new byte[playStr.length()/2];
        for(int i = 0,j=0;i < playStr.length();i+=2,j++){
            String tmp = playStr.substring(i,i+2);
            Integer tmpint = Integer.parseInt(tmp,16);
            data[j] = (byte)(tmpint.intValue());
        }
        return Base64.encodeToString(data);
    }
}
