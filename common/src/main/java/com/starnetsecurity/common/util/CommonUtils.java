package com.starnetsecurity.common.util;

import com.alibaba.fastjson.JSONObject;

import com.starnetsecurity.common.exception.BizException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CM on 2015/4/30.
 */
public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 判空函数
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }

        return false;
    }

    /**
     * 对象数组中是否有空对象
     *
     * @param objects
     * @return
     */
    public static boolean isEmpty(Object... objects) {
        for (Object o : objects) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建带参数的uri
     *
     * @param uri
     * @param paramStr
     * @return
     */
    public static String uriBuilder(String uri, String paramStr) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(uri);
        if (uri.contains("?")) {
            buffer.append("&").append(paramStr);
        } else {
            buffer.append("?").append(paramStr);
        }
        return buffer.toString();
    }

    /**
     * 根据要访问的URI构建对应的完整网络地址
     *
     * @param request
     * @param response
     * @param uri
     * @return
     */
    public static String getServiceUrl(HttpServletRequest request, HttpServletResponse response, String uri) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(request.isSecure() ? "https://" : "http://");
        buffer.append(request.getServerName());
        buffer.append(uri);
        return response.encodeURL(buffer.toString());
    }


    /**
     * 获取当前页面完整链接
     *
     * @param request
     * @return
     */
    public static String fullUrlBuild(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        StringBuffer buffer = new StringBuffer();
        buffer.append(request.getScheme()).append("://").append(request.getServerName())
//                .append(":") .append(request.getServerPort())
                .append(contextPath).append(uri);
        String queryStr = request.getQueryString();
        if (!CommonUtils.isEmpty(queryStr)) {
            buffer.append("?").append(queryStr);
        }
        String fullUrl = buffer.toString();
        return fullUrl;
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getMorningTs(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp dateToToday(Timestamp timestamp){
        Timestamp currentTime = getTimestamp();
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
        DateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Timestamp time = Timestamp.valueOf(sdf1.format(currentTime) + sdf2.format(timestamp));
        return time;
    }

    public static String formatTimeStamp(String format,Timestamp timestamp){
        //yyyy-MM-dd HH:mm:ss
        DateFormat sdf1 = new SimpleDateFormat(format);
        return sdf1.format(timestamp);
    }

    public static Timestamp getTodayStartTimeStamp(){
        Timestamp currentTime = getTimestamp();
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Timestamp time = Timestamp.valueOf(sdf1.format(currentTime));
        return time;
    }
    public static Timestamp getTodayEndTimeStamp(){
        Timestamp currentTime = getTimestamp();
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Timestamp time = Timestamp.valueOf(sdf1.format(currentTime));
        return time;
    }

    public static Timestamp getTomorrowStartTimeStamp(){
        Timestamp currentTime = getTimestamp();
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Timestamp time = Timestamp.valueOf(sdf1.format(currentTime));
        time = new Timestamp(time.getTime() + 1000);
        return time;
    }

    public static Timestamp getWeekStartTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(cal.getTime());
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    public static Timestamp getMonthStartTimeStamp(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String time = sdf.format(c.getTime());
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    public static Timestamp getMonthEndTimeStamp(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String time = sdf.format(ca.getTime());
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    public static int getDayOfWeekFromTimeStamp(Timestamp timestamp){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timestamp.getTime()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;
    }

    public static int getTodayWeekDayNum(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;
    }

    public static Map uploadFile(String relativePath,MultipartFile file, HttpServletRequest request) throws BizException{
        Map<String,Object> result = new HashMap<String, Object>();
        String path = request.getSession().getServletContext().getRealPath("upload");
        path += relativePath;
        String uploadFileName = file.getOriginalFilename();
        if(uploadFileName.indexOf("xls") < 0){
            throw new BizException("请提交excel表格文件");
        }
        if(Util.isEmpty(uploadFileName)){
            throw new BizException("文件内容为空");
        }
        String[] suffix = uploadFileName.split("\\.");
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix[suffix.length-1];

        File targetFile = new File(path, fileName);
        targetFile.setWritable(true,false);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("文件IO存储异常");
        }
        result.put("fileName",uploadFileName.split("\\.")[0]);
        result.put("realPath",path + System.getProperty("file.separator") + fileName);
        result.put("serverPath",request.getContextPath()+"/upload" + relativePath.replaceAll("\\\\","/") + "/" + fileName);
        return result;
    }

    public static int cnWeekDayToNum(String weekDay){
        if(weekDay.equals("星期一")){
            return 1;
        }else if(weekDay.equals("星期二")){
            return 2;
        }else if(weekDay.equals("星期三")){
            return 3;
        }else if(weekDay.equals("星期四")){
            return 4;
        }else if(weekDay.equals("星期五")){
            return 5;
        }else if(weekDay.equals("星期六")){
            return 6;
        }else if(weekDay.equals("星期日")){
            return 7;
        }else{
            return 0;
        }
    }

    /**
     * 获取客户端访问IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }
    /**
     * 调用控制台ping某个IP
     * begin
     */
    public static int getCheckResult(String line){
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTl=\\d+)",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
            return 1;
        }
        return 0;
    }

    public static boolean ping(String ipAddress){
        return ping(ipAddress,4,4000);
    }

    public static boolean ping(String ipAddress,int timeOut){
        return ping(ipAddress,4,timeOut);
    }

    public  static boolean ping(String ipAddress,int pingTimes,int timeOut){
        BufferedReader in = null;
        Runtime runtime = Runtime.getRuntime();
        String pingCommand = "ping "+ ipAddress + " -n " + pingTimes + " -w "+ timeOut;
        try{
            Process process = runtime.exec(pingCommand);
            if(isEmpty(process)){
                return false;
            }
            in  = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            int connectedCount = 0;
            String line = null;
            while((line = in.readLine()) != null){
                connectedCount += getCheckResult(line);
            }
            return connectedCount == pingTimes;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }finally {
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 调用控制台ping某个IP
     * end
     */

    /**
     * 关于时间的运算
     */
    public static Date IncMinute(Date originalTime,int calMinute) throws ParseException {
        Date resDate = new Date();
        Date tempTime = originalTime;
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        Calendar cal = Calendar.getInstance();
        cal.setTime(tempTime);
        cal.add(Calendar.MINUTE,calMinute);
        String dateStr = sdf.format(cal.getTime());
        resDate = sdf.parse(dateStr);
        return resDate;

    }

    public static String uploadImage(String path,String FolderName,MultipartFile file, HttpServletRequest request) throws BizException{
        path += File.separator + FolderName + File.separator;
        String dateStr = formatTimeStamp("yyyy-MM-dd",getTimestamp());
        path += dateStr;
        String uploadFileName = file.getOriginalFilename();
        if(uploadFileName.indexOf("jpg") < 0 && uploadFileName.indexOf("bmp") < 0 && uploadFileName.indexOf("png") < 0){
            throw new BizException("图片格式不正确");
        }
        if(Util.isEmpty(uploadFileName)){
            throw new BizException("文件内容为空");
        }
        String[] suffix = uploadFileName.split("\\.");
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix[suffix.length-1];

        File targetFile = new File(path, fileName);
        targetFile.setWritable(true,false);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("文件IO存储异常");
        }

        return (dateStr + File.separator + fileName).replaceAll("\\\\","/");
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public static String saveCarPlateImage(String relativePath,String base64FileStr, String realPath, String contextPath,String ip,String date,String fileName) throws BizException{

        String nowFomat = date;
        OutputStream os = null;
        String path = realPath + File.separator + "resources" + File.separator + relativePath + File.separator + nowFomat
                + File.separator + ip;
        if(StringUtils.isEmpty(base64FileStr)){
            return "";
        }
        fileName += ".jpg" ;

        File targetFile = new File(path);
        targetFile.setWritable(true,false);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            os = new FileOutputStream(targetFile.getPath() + File.separator + fileName);
            os.write(org.apache.commons.codec.binary.Base64.decodeBase64(base64FileStr), 0, org.apache.commons.codec.binary.Base64.decodeBase64(base64FileStr).length);
        } catch (IOException e) {
            return "";
        } catch (Exception e) {
            return "";
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                return "";
            }
        }
        return contextPath + "/resources/" + relativePath + "/" + nowFomat + "/" + ip + "/" + fileName;
    }

    public static String getRuestIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

    public static String getDateUnitId(){
        Random random = new Random();
        int a = random.nextInt(9) + 1;

        String res = "";
        res += a;
        for(int i = 0; i < 12; i++){
            int b = (int)(10*(Math.random()));
            res += b;
        }
        return "w" + formatTimeStamp("yyyyMMdd",getTimestamp()) + res;
    }
}