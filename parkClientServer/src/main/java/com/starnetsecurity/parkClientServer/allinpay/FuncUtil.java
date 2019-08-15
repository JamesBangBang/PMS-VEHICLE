package com.starnetsecurity.parkClientServer.allinpay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings({ "rawtypes","unchecked"})
public class FuncUtil {
	
	private static DecimalFormat df=new DecimalFormat("#0.00"); 
	
	public static String[] split(String line,char split_char) {
		List segments = new ArrayList();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == split_char) {
				segments.add(dropNull(sb.toString()));
				sb = new StringBuffer();
			} else {
				sb.append(c);
			}
		}
		segments.add(sb.toString());

		return (String[]) segments.toArray(new String[0]);
	}
	
	public static String dropNull(String input) {
		if (input == null) {
			input = "";
		}
		return input.trim();
	}
	
	public static String formatTime(Date date,String formatter){
		SimpleDateFormat sdf  = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}
	
	public static String formatTime(Timestamp date,String formatter){
		if(date==null)
	        return "";
		SimpleDateFormat sdf  = new SimpleDateFormat(formatter);
		Date date_new = new Date(date.getTime());
		return sdf.format(date_new);
	}
	
	public static Date parseTime(String datestr,String formatter) throws Exception{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatter);
			return sdf.parse(datestr);
		}
		catch(Exception ex){
			Exception oe = new Exception("时间解析错误:" + ex.getMessage());
			throw oe;
		}
	}
	
	public static Timestamp parseStampTime(String datestr,String formatter) throws Exception{
		if(datestr==null) return null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatter);
			return new Timestamp(sdf.parse(datestr).getTime());
		}
		catch(Exception ex){
			Exception oe = new Exception("时间解析错误:" + ex.getMessage());
			throw oe;
		}
	}
	
	public static Timestamp parseStampTime1(String datestr){
		if(isEmpty(datestr))
			return null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return new Timestamp(sdf.parse(datestr).getTime());
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public static Timestamp getCurrTimestamp(){
		return new Timestamp(new Date().getTime());
	}

	
	public static boolean isNum(String str) {
		boolean flag = false;
		Pattern pat = Pattern.compile("^[0-9]*$");
		Matcher mat = null;
		mat = pat.matcher(str);
		flag = mat.matches();
		return flag;
	}
	
	/**
	 * 判断是否为数字，包括小数
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		if(isEmpty(str))
			return false;
		boolean flag = false;
		Pattern pat = Pattern.compile("-?[0-9]*.?[0-9]*");
		Matcher mat = null;
		mat = pat.matcher(str);
		flag = mat.matches();
		return flag;
	}
	
	public static BigDecimal ftoy(BigDecimal num){
		return num.divide(new BigDecimal(100), 2, BigDecimal.ROUND_CEILING);
	}
	
	public static BigDecimal ftoy(long num){
		return ftoy(new BigDecimal(num));
	}
	
	public static BigDecimal ftoy(String num){
		return ftoy(new BigDecimal(num));
	}
	
	public static BigDecimal ytof(String y){
		return new BigDecimal(Math.round(new BigDecimal(y).multiply(new BigDecimal(100)).doubleValue()));
//		if(y.charAt(0) == '.') {
//			y = '0' + y;
//		}
//		String[] ps = y.split("\\.");
//		long ttp = Long.parseLong(ps[0]);
//		long r = ttp*100;
//		if (ps.length == 2){
//			if (ps[1].length() == 1)
//				r += Long.parseLong(ps[1])*10;
//			else if (ps[1].length() == 2)
//				r += Long.parseLong(ps[1]);
//			else {
//				r += Long.parseLong(ps[1].substring(0, 2));
//				if (ps[1].charAt(2) > '4')
//					r++;
//			}
//		}
//		return new BigDecimal(r);
	}	
	
	public static BigDecimal ytof(double y){
		return ytof(String.valueOf(y));
	}
	
	public static BigDecimal ytof(BigDecimal y){
		return ytof(y.toString());
	}
	
	//Map排序
	public static SortedMap<String, String> mapSortByKey(Map<String, String> unsort_map){
		TreeMap<String, String> result = new TreeMap<String, String>();
		Object[] unsort_key = unsort_map.keySet().toArray();
		Arrays.sort(unsort_key);
		for (int i = 0; i < unsort_key.length; i++) {
			result.put(unsort_key[i].toString(), unsort_map.get(unsort_key[i]));
		}
		return result.tailMap(result.firstKey());
	}	
	
	/**
	 * 构建in字符串
	 * @param str
	 * @return
	 */
	public static String buildInStr(Object[] str){
		if(str==null||str.length==0)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<str.length;i++){
			if(str[i]==null||str[i].toString().trim().length()==0)
				continue;
			sb.append("'");
			sb.append(str[i]);
			sb.append("',");
		}
		String tmp = sb.toString();
		if(tmp!=null&&tmp.length()>1)
			return tmp.substring(0,tmp.length()-1);
		else
			return null;
	}
	
	/**
	 * 构建in字符串
	 * @param str
	 * @return
	 */
	public static String buildInStrNoDH(Object[] str){
		if(str==null||str.length==0)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<str.length;i++){
			if(str[i]==null||str[i].toString().trim().length()==0)
				continue;
			sb.append(str[i]);
			sb.append(",");
		}
		String tmp = sb.toString();
		if(tmp!=null&&tmp.length()>1)
			return tmp.substring(0,tmp.length()-1);
		else
			return null;
	}	
	
	/**
	 * 构建in字符串
	 * @param str
	 * @param spliter
	 * @return
	 */
	public static String buildInStr(String str,String spliter){
		if(str==null||str.trim().length()==0)
			return null;
		return FuncUtil.buildInStr(str.split(spliter));
	}
	
	/**
	 * 构建in字符串
	 * @param list
	 * @return
	 */
	public static String buildInStr(List list){
		if(list==null||list.size()==0)
			return null;
		return FuncUtil.buildInStr(list.toArray());
	}	
	
	/**
	 * 构建in字符串
	 * @param str
	 * @param spliter
	 * @return
	 */
	public static String buildInStr(String str,char spliter){
		if(str==null||str.trim().length()==0)
			return null;
		return FuncUtil.buildInStr(str.split(String.valueOf(spliter)));
	}
	
	/**
	 * 获取时间偏移后时间量
	 * @param date
	 * @param offset
	 * @return
	 */
	public static Date getTimeOffset(Date date,long offset){
		if(date==null)
			return null;
		return new Date(date.getTime()+offset);
	}
	
	public static boolean isEmpty(String s){
		if(s==null||"".equals(s.trim()))
			return true;
		return false;
	}
	
	/**
	 * 是否为空数组
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(Object[] val) {
		if (val == null || val.length == 0) {
			return true;
		}
		for (int i = 0; i < val.length; i++) {
			if (val[i] != null && !"".equals(val[i])) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 是否为空数组
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List list) {
		return null==list||list.size()==0;
	}
	
	/**
	 * 检查手机
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobile(String mobile) {
		if (isEmpty(mobile)) {
			return true;
		}
		Pattern pattern = Pattern.compile("^(1[3|4|5|8|9])\\d{9}$");
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	/**
	 * 四位随机码
	 * @return
	 */
	public static String getRandcode(int n) {
		Random random = new Random();
		String sRand = "";
		n = n == 0 ? 4 : n;// default 4
		for (int i = 0; i < n; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}
	
	/**
	 * 根据路径创建一系列的目录
	 * @param path
	 */
	public static void mkDir(String path) {
		File file;
		try {
			file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
	}	
	
	/**
	 * 文件名过滤特殊字符
	 * @param str
	 * @return
	 */
	public  static String SpecStrFilter(String str){
		String regEx="[*:\\/?<>|\"]";   
		Pattern   p=Pattern.compile(regEx);      
		Matcher   m=p.matcher(str);      
		return   m.replaceAll("_").trim();
	}
	
	public static boolean removeFile(String path){
		return removeFile(new File(path));
	}

	/**
	 * 删除文件
	 * @param path
	 * @return
	 */
	public static boolean removeFile(File path){
		boolean result = false;
		System.out.println("删除文件:"+path.getPath());
		if (path.isDirectory()){
			File[] child = path.listFiles();
			if (child != null && child.length != 0){
				for (int i = 0; i < child.length; i++){
					if(removeFile(child[i])==false){
						System.out.println("删除文件:"+child[i]+" 失败");
						return false;
					}
				    child[i].delete();
				 }
			}
		}
		path.delete();
		result = true;
		return result;
	}	
	
	/**
	 * 格式化数字0.00
	 * @param number
	 * @return
	 */
	public static String formatNumber(BigDecimal number){
		return formatNumber(number.doubleValue());
	}
	
	/**
	 * 格式化数字0.00
	 * @param number
	 * @return
	 */
	public static String formatNumber(double number){
		DecimalFormat df1=(DecimalFormat) DecimalFormat.getInstance();
		df1.applyPattern("0.00");
		return df1.format(number);
	}
	
	/**
	 * 格式化数字，默认是#,##0.00
	 * @param number
	 * @return
	 */
	public static String formatNumber1(BigDecimal number,String pattern){
		return formatNumber1(number.doubleValue(),pattern);
	}
	
	/**
	 * 格式化数字，默认是#,##0.00
	 * @param number
	 * @return
	 */
	public static String formatNumber1(double number,String pattern){
		DecimalFormat df1=(DecimalFormat) DecimalFormat.getInstance();
		if(FuncUtil.isEmpty(pattern)){
			pattern = "#,##0.00";
		}
		df1.applyPattern(pattern);
		return df1.format(number);
	}
	
	public static BigDecimal parseString(String source,String pattern) throws ParseException{
		DecimalFormat df1=(DecimalFormat) DecimalFormat.getInstance();
		if(FuncUtil.isEmpty(pattern)){
			pattern = "#,##0.00";
		}
		df1.applyPattern(pattern);
		return new BigDecimal(df1.parse(source).doubleValue());
	}
	
	/**
	 * 屏蔽账号 开头保留多少位 末尾保留多少位
	 * @return
	 */
	public static String shieldAccount(int head,int end,boolean isshield,String account){
		if(account == null||"".equals(account)){
			return "";
		}
		int total = head+end;
		if(account.length()<=total){
			if(isshield){//当位数不够的时候，是否屏蔽所有的
				int length = account.length();
				StringBuilder sb = new StringBuilder();
				while(length>0){
					sb.append("*");
					length--;
				}
				return sb.toString();
			}else{
				return account;
			}
		}
//		int sAccount = account.length()-total;//屏蔽成*的个数
		int sAccount = 4;
		StringBuilder sb = new StringBuilder();
		while(sAccount>0){
			sb.append("*");
			sAccount--;
		}
		return account.substring(0,head)+sb.toString()+account.substring(account.length()-end);
	}
	
	/***
	 * 检查身份证
	 * */
	public static boolean checkIdcard(String idcard){
		boolean valid = false;
		if(idcard!=null && !"".equals(idcard.trim())){
			if(idcard.length()==18){
				valid = isNum(idcard.substring(0, idcard.length()-1));
				if(valid){
					valid = isNum(idcard.substring(idcard.length()-1));
					if(!valid){
						if(idcard.endsWith("x") || idcard.endsWith("X")){
							valid = true;
						}
					}
				}
			}else if(idcard.length()==15){
				valid = isNum(idcard);
			}
		}
		return valid;
	}
	
	public static String join(String[] array){
		if(array == null ||array.length==0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(String s: array){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static String getFileDealType(String fileName) throws Exception {
		if(fileName==null||fileName.equals(""))
			return null;
		int subpos=fileName.indexOf("_");
		if(subpos>0){
			return fileName.substring(subpos+1,subpos+2);	
		}
		return null;
	}
	
	/**
	 * 判断汉字
	 * comment here
	 * @param strVal
	 * @return
	 */
	public static boolean isChinese(String strVal) {
		int iRnt = 0;
		boolean rs=false;
		for (int i=0; i<strVal.length(); i++) {			
			String strHanzi = strVal.substring(i, i+1);
			byte[] bytes = null;
			try {
				bytes = (strHanzi).getBytes("gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
			if (bytes == null || bytes.length > 2 || bytes.length <= 0
					|| bytes.length == 1) {
				//非汉字
				iRnt++;
			}
			if (bytes.length == 2) { // 汉字
				return true;
			}
		}//for
		rs=false;
		return rs;
	}
	
	public static boolean isUseSign(String tp){
		if(tp==null||tp.trim().equals("1")||tp.trim().equals("0"))
			return false;
		else
			return true;
	}
	
	public static int bgCompare(BigDecimal val1,BigDecimal val2){
		return val1.compareTo(val2);
	}
	
	public static byte[] getBytesFromIS(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b = 0;
		// BufferedReader br = new BufferedReader(new InputStreamReader(input,
		//		"GBK"));
		while ((b = is.read()) != -1)
			baos.write(b);

		return baos.toByteArray();
	}
	
	/**
	 * 删除文件夹及下所有文件
	 * @param dir
	 * @return
	 */
	public static boolean deleteFolder(File dir) {
		File filelist[] = dir.listFiles();
		int listlen = filelist.length;
		for (int i = 0; i < listlen; i++) {
			if (filelist[i].isDirectory()) {
				deleteFolder(filelist[i]);
			} else {
				if (!filelist[i].delete())
					return false;
			}
		}
		if (!dir.delete())
			return false;// 删除当前目录
		else
			return true;
	}
	
	/**
	 * 四舍五入处理
	 * @param pos 保留小数位
	 * @param bd
	 * @return BigDecimal
	 */
	public static BigDecimal setScale(int pos,BigDecimal bd){
		if(bd==null)return null;
		return bd.setScale(pos, BigDecimal.ROUND_HALF_UP);
	}
	
	
	/**
	 * 格式化金额
	 */
	public static String fmtMoney(Object obj)
	{
		try
		{
			Double d=Double.valueOf(obj!=null ? obj.toString() : "0.00");
			return df.format(d);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return df.format(0.00);
		}
	}
	
	
	public static BigDecimal toDecimal(String str, String divide) {
		if (str == null || str.trim().length()==0) {
			str = "0";
		}
		BigDecimal bdRnt = new BigDecimal(str).divide(new BigDecimal(divide),2,BigDecimal.ROUND_HALF_UP);
		return bdRnt;
	}
	
	public static BigDecimal toDecimalMut(String str, String muti) {
		if (str == null || str.trim().length()==0) {
			str = "0";
		}
		BigDecimal bdRnt = new BigDecimal(str).multiply(new BigDecimal(muti));
		return bdRnt;
	}	
	//百分格式化
	public static String Round(BigDecimal num)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		String str = df.format(num);
		return str;
	}

	public static String RoundAndScale(BigDecimal num)
	{
		num = num.divide(new BigDecimal("100.00"));
		DecimalFormat df = new DecimalFormat("0.00");
		String str = df.format(num);
		return str;
	}

	public static String RoundAndScalePer(BigDecimal num)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		String str = df.format(num.multiply(new BigDecimal("100.00")));
		return str;
	}
	
	/**
	 * 获得文件SHA1摘要信息
	 * @param file
	 * @return String
	 * @author l_ghui
	 * @date 2009-10-12
	 */
	public static String getSha1Str(File file){
		String str="";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			int c;
			while ((c = fis.read(buf)) > 0){
				baos.write(buf, 0, c);
			}
			str=new BASE64Encoder().encode(md.digest(baos.toByteArray()));
			fis.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 获得文件SHA1摘要信息
	 * @param bts
	 * @return String
	 * @author l_ghui
	 * @date 2009-10-12
	 */
	public static String getSha1Str(byte[] bts){
		String str="";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			str=new BASE64Encoder().encode(md.digest(bts));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return str;
	}	
		
	
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i]<0 ? src[i] + 256 : src[i];
            String hv = Integer.toHexString(v);

            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    
    private static Field[] getAllDeclaredFields(Class c) {

        if (c.equals(Object.class)) {
            return null;
        }

        Field[] parentFields = getAllDeclaredFields(c.getSuperclass());
        Field[] thisFields = c.getDeclaredFields();
        
        if ((parentFields != null) && (parentFields.length > 0)) {
        	Field[] allFields =
                new Field[parentFields.length + thisFields.length];
            System.arraycopy(parentFields, 0, allFields, 0,
            				parentFields.length);
            System.arraycopy(thisFields, 0, allFields, parentFields.length,
            				thisFields.length);

            thisFields = allFields;
        }

        return thisFields;
    }
    
    /**
     * 截取字符串字节数，中文向前截取
     * @param str
     * @param subSLength
     * @return
     */
    public static String subStr(String str, int subSLength){    
		if (str == null) 
		    return "";
		else{
			try{
				byte[] strbyte = str.getBytes("GBK");
				int subStrByetsL = strbyte.length;//截取子串的字节长度 
				if(subStrByetsL<=subSLength){
					return str;
				}
			    String subStr = new String(strbyte,0,subSLength+1,"GBK");
			    subStr = subStr.substring(0,subStr.length()-1);
			    return subStr;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}   
	}  
    
    public static int chineseCompare(Object _oChinese1, Object _oChinese2) {
        return Collator.getInstance(Locale.CHINESE).compare(_oChinese1,
                _oChinese2);
    }
    
    /** 
     * 检查整数 
     * @param num 
     * @param type "0+":非负整数 "+":正整数 "-0":非正整数 "-":负整数 "":整数 
     * @return 
     */  
    public static boolean checkNumber(String num,String type){  
        String eL = "";  
        if(type.equals("0+"))eL = "^\\d+$";//非负整数  
        else if(type.equals("+"))eL = "^\\d*[1-9]\\d*$";//正整数  
        else if(type.equals("-0"))eL = "^((-\\d+)|(0+))$";//非正整数  
        else if(type.equals("-"))eL = "^-\\d*[1-9]\\d*$";//负整数  
        else eL = "^-?\\d+$";//整数  
        Pattern p = Pattern.compile(eL);  
        Matcher m = p.matcher(num);  
        boolean b = m.matches();  
        return b;  
    } 

	public static <T1,T2> List<T2> convertMap2List(Map<T1,T2> map){
		if(map==null) return null;
		return new ArrayList<T2>(map.values());
	}
    
    public static long getTimeGap(Date dt1,Date dt2){
    	return dt1.getTime()-dt2.getTime();
    }
    
    public static long getTimeGap(String fmt1,String t1,String fmt2,String t2) throws Exception{
    	SimpleDateFormat sdf1 = new SimpleDateFormat(fmt1);
    	Date dt1 = sdf1.parse(t1);
    	SimpleDateFormat sdf2 = new SimpleDateFormat(fmt2);
    	Date dt2 = sdf2.parse(t2);
    	return FuncUtil.getTimeGap(dt1, dt2);
    }
    
    public static boolean compareByteArr(byte[] arr1,byte[] arr2)
    {
    	if(arr1.length!=arr2.length)
    		return false;
    	for(int i=0;i<arr1.length;i++)
    	{
    		if(arr1[i]!=arr2[i])
    			return false;
    	}
    	return true;
    }
    
    //appid=null&cusid=990581048996000类似这种转化为map
    public static Map getMapFromStringParam(String param){
    	if(FuncUtil.isEmpty(param))
    		return null;
    	String[] array = param.split("&");
    	Map<String, String> map = new HashMap<String, String>();
    	for(String item:array){
    		String[]temp = item.split("=");
    		String key = temp[0];
    		String value = "";
    		if(temp.length>1&&!temp[1].toLowerCase().equals("null")){
    			value = temp[1];
    		}
    		map.put(key, value);
    	}
    	return map;
    }
    
    public static <T> T formMaptoBean(T obj,Map<String,Object> map,Class<T> clazz) throws Exception{
    	return formMaptoBean(obj, map, clazz, null);
    }
    
    public static <T> T formMaptoBean(T obj,Map<String,Object> map,Class<T> clazz,String timeformatter) throws Exception{
    	if(obj == null){
			obj = clazz.newInstance();
		}
    	
    	Map<String,Field> fieldMap = new HashMap<String,Field>();
    	Field[] fields = getAllDeclaredFields(clazz);
    	
    	if(fields==null) return obj;
    		
    	for(Field field : fields){
    		fieldMap.put(field.getName(),field);
    	}
    	   	
    	
	  	for(Map.Entry<String, Object> entry : map.entrySet()){
	  		String key = entry.getKey();
	  		Object value = entry.getValue();
	  		try{	  			
	  			String mothedName = "set"+key.substring(0, 1).toUpperCase()+key.substring(1);
	  			Class valueClass = fieldMap.get(key)==null?null:fieldMap.get(key).getType();
	  			if(valueClass==null) continue;
	  			Method method = clazz.getMethod(mothedName,valueClass);
	  			
	  			if(method != null)
	  				method.invoke(obj, getObject(valueClass,value,timeformatter));
	  		}catch(Exception e){
	  		}
	  	}
		
		return obj;
	}
    private static Object getObject(Class<?> clzz,Object value,String timeformatter) throws Exception{
    	//如果是包装类
    	if(value==null) return null;
    	if(clzz==Long.class){  
    		
    		return new Long(value.toString());
    	}
    	else if(clzz==Boolean.class){
    		return new Boolean(value.toString());
    	}
    	else if(clzz==Byte.class){
    		return new Byte(value.toString());
    	}
    	else if(clzz==Short.class){
    		return new Short(value.toString());
    	}
    	else if(clzz==Integer.class){
    		return new Integer(value.toString());
    	}
    	else if(clzz==Float.class){
    		return new Float(value.toString());
    	}
    	else if(clzz==Double.class){
    		return new Double(value.toString());
    	}
    	else if(clzz==String.class){
    		return new String(value.toString());
    	}
    	//如果是基本类
    	else if(clzz==byte.class){    		
    		return  Byte.parseByte(value.toString());
    	}
    	else if(clzz==short.class){    		
    		return  Short.parseShort(value.toString());
    	}
    	else if(clzz==int.class){    		
    		return  Integer.parseInt(value.toString());
    	}
    	else if(clzz==long.class){    		
    		return  Long.parseLong(value.toString());
    	}
    	else if(clzz==float.class){    		
    		return  Float.parseFloat(value.toString());
    	}
    	else if(clzz==double.class){    		
    		return  Double.parseDouble(value.toString());
    	}
    	else if(clzz==boolean.class){    		
    		return  Boolean.parseBoolean(value.toString());
    	}
    	
    	//java.sql.Timestamp
    	if(clzz==java.sql.Timestamp.class){
    		return  parseStampTime(value.toString(), timeformatter);
    	}

    	//java.util.Date
    	if(clzz==java.util.Date.class){
    		return  parseTime(value.toString(), timeformatter);
    	}
    	return value;
    }
    
    public static String getMapValue(Map param,String key){
		if(param.containsKey(key))
			return (String)param.get(key);
		return "";
	}

    
    public static String md5(byte[] b) {
        try {
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	 md.reset();
             md.update(b);
             byte[] hash = md.digest();
             StringBuffer outStrBuf = new StringBuffer(32);
             for (int i = 0; i < hash.length; i++) {
                 int v = hash[i] & 0xFF;
                 if (v < 16) {
                 	outStrBuf.append('0');
                 }
                 outStrBuf.append(Integer.toString(v, 16).toLowerCase());
             }
             return outStrBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new String(b);
        }
       
    }
    
    public static boolean validSign(TreeMap<String,String> param,String appkey) throws Exception{
    	if(param!=null&&!param.isEmpty()){
    		if(!param.containsKey("sign")){
    			return false;
    		}
    		param.put("key", appkey);//将分配的appkey加入排序
    		StringBuilder sb = new StringBuilder();
    		String sign = param.get("sign").toString();
    		param.remove("sign");
    		for(String key:param.keySet()){
    			String value = param.get(key);
    			if(!FuncUtil.isEmpty(value))
    				sb.append(key).append("=").append(value).append("&");
    		}
    		if(sb.length()>0){
    			sb.deleteCharAt(sb.length()-1);
    		}
    		String blank = sb.toString();
			String blankSign = md5(blank.getBytes("utf-8"));
			String signStr = sign.toLowerCase();
    		return sign.toLowerCase().equals(blankSign);
    	}
    	return false;
    }
    
    public static String toJsonResult(BaseRsp rsp){
		try{
			Field[] fields = rsp.getClass().getFields();
			Map<String,String> map = new TreeMap<String,String>();
			for(Field field : fields){
				Object val = field.get(rsp);
				if(val==null||val.toString().equals("")){
					continue;
				}
				map.put(field.getName(), val.toString());
			}
			StringBuilder sb = new StringBuilder();
			map.put("key", AppInfo.allinpayKey);
			for(Map.Entry<String, String> entry:map.entrySet()){
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
			String blank = sb.toString();
			String sign = md5(blank.getBytes("UTF-8"));
			System.out.println(blank+";"+sign);
			map.put("sign", sign);
			map.remove("key");//加签完毕后把key字段删除
			return map.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
}
