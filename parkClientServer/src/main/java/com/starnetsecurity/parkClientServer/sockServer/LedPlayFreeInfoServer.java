package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.commonService.util.DeviceLedCgiThread;
import com.starnetsecurity.commonService.util.DeviceLedFourThread;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.entity.LedDisplayConfig;
import com.starnetsecurity.parkClientServer.service.DeviceBizService;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by JAMESBANG on 2018/8/11.
 */
public class LedPlayFreeInfoServer extends Thread {
    private static String control = "";

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
       boolean isNeedAdd = true;           //是否需要新增记录
       if (playInfo.size() > 0){
           String playType = playInfo.get("playType") + "";
           if ("three".equals(playType)) {
               DeviceInfo deviceInfo = (DeviceInfo) playInfo.get("deviceInfo");
               String deviceId = deviceInfo.getDeviceId();
               String remainStr = "";
               String playContentFirst = playInfo.get("playContentFirst") + "";
               String playContentSecond = playInfo.get("playContentSecond") + "";
               String playContentThird = playInfo.get("playContentThird") + "";
               if (playContentFirst.indexOf("空位") >= 0) {
                   remainStr = playContentFirst;
               } else if (playContentSecond.indexOf("空位") >= 0) {
                   remainStr = playContentSecond;
               } else if (playContentThird.indexOf("空位") >= 0) {
                   remainStr = playContentThird;
               }
               playInfo.put("remainStr", remainStr);
               for (int i = 0; i < playList.size(); i++) {
                   playList.get(i).put("remainStr", remainStr);
                   DeviceInfo deviceInfoBefore = (DeviceInfo) playList.get(i).get("deviceInfo");
                   String deviceIdBefore = deviceInfoBefore.getDeviceId();
                   if (deviceIdBefore.equals(deviceId)) {
                       playList.set(i, playInfo);    //将第i个元素替换
                       isNeedAdd = false;
                   }
               }
           }else {
               DeviceInfo deviceInfo = (DeviceInfo) playInfo.get("deviceInfo");
               String deviceId = deviceInfo.getDeviceId();
               String remainStr = "";
               String playContentFirst = playInfo.get("playContentFirst") + "";
               String playContentSecond = playInfo.get("playContentSecond") + "";
               String playContentThird = playInfo.get("playContentThird") + "";
               if (playContentFirst.indexOf("BFD5CEBB") >= 0) {
                   remainStr = playContentFirst;
               } else if (playContentSecond.indexOf("BFD5CEBB") >= 0) {
                   remainStr = playContentSecond;
               } else if (playContentThird.indexOf("BFD5CEBB") >= 0) {
                   remainStr = playContentThird;
               }
               playInfo.put("remainStr", remainStr);
               for (int i = 0; i < playList.size(); i++) {
                   playList.get(i).put("remainStr", remainStr);
                   DeviceInfo deviceInfoBefore = (DeviceInfo) playList.get(i).get("deviceInfo");
                   String deviceIdBefore = deviceInfoBefore.getDeviceId();
                   if (deviceIdBefore.equals(deviceId)) {
                       playList.set(i, playInfo);    //将第i个元素替换
                       isNeedAdd = false;
                   }
               }
           }
       }
       //更新led的播放时间
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
                        for (int i = 0; i < playList.size(); i++) {
                            JSONObject needPlayInfo = playList.get(i);
                            String playType = needPlayInfo.get("playType") + "";
                            if ("three".equals(playType)) {
                                String playTime = needPlayInfo.get("playTime") + "";
                                Timestamp ledPlayTime = Timestamp.valueOf(playTime);
                                String isNeedPlay = needPlayInfo.get("isNeedPlay") + "";
                                String remainStr = needPlayInfo.get("remainStr") + "";
                                if ("true".equals(isNeedPlay)) {
                                    //超过30秒
                                    long timeNow = CommonUtils.getTimestamp().getTime() / 1000;
                                    long timePlay = ledPlayTime.getTime() / 1000;
                                    int timeDistance = (int) (timeNow - timePlay);
                                    if (timeDistance > 30) {
                                        DeviceInfo deviceInfo = (DeviceInfo) needPlayInfo.get("deviceInfo");

                                        String playContentFirst = needPlayInfo.get("playContentFirst") + "";
                                        playContentFirst = (!CommonUtils.isEmpty(remainStr) && playContentFirst.indexOf("空位") >= 0) ? remainStr : playContentFirst;
                                        if (playContentFirst.indexOf("年") > 0 && playContentFirst.indexOf("月") > 0 && playContentFirst.indexOf("日") > 0){
                                            Calendar nowTime = Calendar.getInstance();
                                            playContentFirst = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                        }
                                        JSONObject params = new JSONObject();
                                        List<JSONObject> ledParamsList = new ArrayList<>();
                                        params.put("type", 0);
                                        params.put("pos", 1);
                                        params.put("str", playContentFirst);
                                        params.put("bscoll", 1);
                                        params.put("action", "set");
                                        ledParamsList.add(params);

                                        String playContentSecond = needPlayInfo.get("playContentSecond") + "";
                                        playContentSecond = (!CommonUtils.isEmpty(remainStr) && playContentSecond.indexOf("空位") >= 0) ? remainStr : playContentSecond;
                                        if (playContentSecond.indexOf("年") > 0 && playContentSecond.indexOf("月") > 0 && playContentSecond.indexOf("日") > 0){
                                            Calendar nowTime = Calendar.getInstance();
                                            playContentSecond = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                        }
                                        params = new JSONObject();
                                        params.put("type", 0);
                                        params.put("pos", 2);
                                        params.put("str", playContentSecond);
                                        params.put("bscoll", 1);
                                        params.put("action", "set");
                                        ledParamsList.add(params);

                                        String playContentThird = needPlayInfo.get("playContentThird") + "";
                                        playContentThird = (!CommonUtils.isEmpty(remainStr) && playContentThird.indexOf("空位") >= 0) ? remainStr : playContentThird;
                                        if (playContentThird.indexOf("年") > 0 && playContentThird.indexOf("月") > 0 && playContentThird.indexOf("日") > 0){
                                            Calendar nowTime = Calendar.getInstance();
                                            playContentThird = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                        }
                                        params = new JSONObject();
                                        params.put("type", 0);
                                        params.put("pos", 3);
                                        params.put("str", playContentThird);
                                        params.put("bscoll", 1);
                                        params.put("action", "set");
                                        ledParamsList.add(params);

                                        playList.get(i).put("playTime", CommonUtils.getTimestamp().toString());
                                        DeviceLedCgiThread deviceLedCgiThread = new DeviceLedCgiThread();
                                        deviceLedCgiThread.setParamsList(ledParamsList);
                                        deviceLedCgiThread.setUsername(deviceInfo.getDeviceUsername());
                                        deviceLedCgiThread.setPassword(deviceInfo.getDevicePwd());
                                        deviceLedCgiThread.setIp(deviceInfo.getDeviceIp());
                                        deviceLedCgiThread.setPort(Integer.parseInt(deviceInfo.getDevicePort()));
                                        deviceLedCgiThread.start();
                                    }
                                }

                            }else {
                                String playTime = needPlayInfo.get("playTime") + "";
                                Timestamp ledPlayTime = Timestamp.valueOf(playTime);
                                String isNeedPlay = needPlayInfo.get("isNeedPlay") + "";
                                String isFlashTimeOne = needPlayInfo.get("isFlashTimeOne") + "";
                                String isFlashTimeTwo = needPlayInfo.get("isFlashTimeTwo") + "";
                                String isFlashTimeThree = needPlayInfo.get("isFlashTimeThree") + "";
                                String isFlashTimeFour = needPlayInfo.get("isFlashTimeFour") + "";
                                Integer playPos = needPlayInfo.getInteger("playPos");
                                if ("true".equals(isNeedPlay)) {
                                    //超过30秒
                                    long timeNow = CommonUtils.getTimestamp().getTime() / 1000;
                                    long timePlay = ledPlayTime.getTime() / 1000;
                                    int timeDistance = (int) (timeNow - timePlay);
                                    if (timeDistance > 30) {
                                        DeviceInfo deviceInfo = (DeviceInfo) needPlayInfo.get("deviceInfo");

                                        String playContentFirst = needPlayInfo.get("playContentFirst") + "";
                                        if ("1".equals(isFlashTimeOne)){
                                            Calendar nowTime = Calendar.getInstance();
                                            String playContent = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                            playContentFirst = playStrToFourLed(playContent,"0");
                                        }
                                        JSONObject params = new JSONObject();
                                        List<JSONObject> ledParamsList = new ArrayList<>();
                                        params.put("uartID", playPos);
                                        params.put("write_data", playContentFirst);
                                        params.put("need_rspdata", 0);
                                        params.put("action","set");
                                        ledParamsList.add(params);

                                        String playContentSecond = needPlayInfo.get("playContentSecond") + "";
                                        if ("1".equals(isFlashTimeTwo)){
                                            Calendar nowTime = Calendar.getInstance();
                                            String playContent = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                            playContentSecond = playStrToFourLed(playContent,"1");
                                        }
                                        params = new JSONObject();
                                        params.put("uartID", playPos);
                                        params.put("write_data", playContentSecond);
                                        params.put("need_rspdata", 0);
                                        params.put("action","set");
                                        ledParamsList.add(params);

                                        String playContentThird = needPlayInfo.get("playContentThird") + "";
                                        if ("1".equals(isFlashTimeThree)){
                                            Calendar nowTime = Calendar.getInstance();
                                            String playContent = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                            playContentThird = playStrToFourLed(playContent,"2");
                                        }
                                        params = new JSONObject();
                                        params.put("uartID", playPos);
                                        params.put("write_data", playContentThird);
                                        params.put("need_rspdata", 0);
                                        params.put("action","set");
                                        ledParamsList.add(params);

                                        String playContentFour = needPlayInfo.get("playContentFour") + "";
                                        if ("1".equals(isFlashTimeFour)){
                                            Calendar nowTime = Calendar.getInstance();
                                            String playContent = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                                                    nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                                            playContentFour = playStrToFourLed(playContent,"1");
                                        }
                                        params = new JSONObject();
                                        params.put("uartID", playPos);
                                        params.put("write_data", playContentFour);
                                        params.put("need_rspdata", 0);
                                        params.put("action","set");
                                        ledParamsList.add(params);

                                        playList.get(i).put("playTime", CommonUtils.getTimestamp().toString());
                                        DeviceLedFourThread deviceLedFourThread = new DeviceLedFourThread();
                                        deviceLedFourThread.setParamsList(ledParamsList);
                                        deviceLedFourThread.setUsername(deviceInfo.getDeviceUsername());
                                        deviceLedFourThread.setPassword(deviceInfo.getDevicePwd());
                                        deviceLedFourThread.setIp(deviceInfo.getDeviceIp());
                                        deviceLedFourThread.setPort(Integer.parseInt(deviceInfo.getDevicePort()));
                                        deviceLedFourThread.start();
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    private String playStrToFourLed(String playContent,String playIndex){
        String playInfo = "";
        try {
            playInfo = ChangePlayInfoToOx(playContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Integer strLen = playInfo.length() / 2 + 19;
        Integer teLen = playInfo.length() / 2;
        String dlength = (Integer.toHexString(strLen)).length() < 2 ? "0" + Integer.toHexString(strLen) : Integer.toHexString(strLen);
        String textLen = (Integer.toHexString(teLen)).length() < 2 ? "0" + Integer.toHexString(teLen) : Integer.toHexString(teLen);
        String needCheckContent = "0064FFFF62" + dlength.toUpperCase() + "0" + playIndex + "1501000215010300FF00000000000000" + textLen.toUpperCase() + "00"
                + playInfo;
        String crcContent = GetCrcCheckInfo(needCheckContent);
        String finalContent = needCheckContent + crcContent;
        String res = GetLedPlayStrToBase(finalContent);
        return res;
    }

    private static String ChangePlayInfoToOx(String playStr) throws UnsupportedEncodingException{
        byte[] text = playStr.getBytes(Charset.forName("GB2312"));
        if (text == null) {
            return "BBB6D3ADB9E2C1D9";
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[text.length * 2];
        for (int j = 0; j < text.length; j++) {
            int v = text[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String str = new String(hexChars);
        return str.toUpperCase();
    }

    private static String GetCrcCheckInfo(String inputStr){
        int usLen = inputStr.length()/2;
        int[] pucFrame = new int[usLen];
        for (int i = 0;i < usLen;i++){
            pucFrame[i] = Integer.parseInt(inputStr.substring(i*2,i*2+2),16);
        }

        int ucCRCHi = 0xFF;
        int ucCRCLo = 0xFF;
        int i;
        int iIndex = 0;

        for (i = 0; i < usLen; i++){
            iIndex = ucCRCLo ^ pucFrame[i];
            ucCRCLo = ucCRCHi ^ aucCRCHi[iIndex];
            ucCRCHi = aucCRCLo[iIndex];
        }
        int intRes = ucCRCHi << 8 | ucCRCLo;
        String res = Integer.toHexString(intRes);
        switch (res.length()){
            case 1 :
                res = "000" + res;
                break;
            case 2 :
                res = "00" + res;
                break;
            case 3 :
                res = "0" + res;
                break;
        }
        String[] data = res.split("");
        res = data[2] + data[3] + data[0] + data[1];
        return res.toUpperCase();
    }

    private static String GetLedPlayStrToBase(String playStr){
        byte[] data = new byte[playStr.length()/2];
        for(int i = 0,j=0;i < playStr.length();i+=2,j++){
            String tmp = playStr.substring(i,i+2);
            Integer tmpint = Integer.parseInt(tmp,16);
            data[j] = (byte)(tmpint.intValue());
        }
        return Base64.encodeToString(data);
    }
}
