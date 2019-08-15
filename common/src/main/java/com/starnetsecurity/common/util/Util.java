/*$Id: Util.java,v 1.6 2010/11/15 08:02:07 cvsAdmin Exp $
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * author   date   comment
 * feitianbubu  2008-4-14  Created
 */
package com.starnetsecurity.common.util;

import com.starnetsecurity.common.exception.BizException;
import org.apache.shiro.codec.*;
import org.apache.shiro.codec.Base64;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * 
 * @author feitianbubu
 */
public class Util implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 获取绝对物理路径
     * 
     * @param path
     *            相对路径
     * @return 对应的绝对物理路径字符串
     */
    public static String getRealPath(String path) {
	return new File(Util.class.getClassLoader().getResource("").getFile()).getParentFile().getParentFile().getPath() + path;
    }

    /**
     * 获取网站的BasePath
     * 
     * @return 网站的BasePath的字符串
     */
    public static String getBasePath(HttpServletRequest request) {
	String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	return path;
    }

    /**
     * 对象是否为空
     * 
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
	return toString(o).equals("");
    }

    public static String toString(Object o) {
	return toString(o, "");
    }

    public static String toString(Object o, String defaultValue) {
        String str = "";
        if (o == null) {
            str = defaultValue;
        } else {
            str = o.toString();
        }
        return str;
    }

    public static Integer toInt(Object o) {
	return toInt(o, 0);
    }

    public static Integer toInt(Object o, int defaultValue) {
	int i = defaultValue;
	try {
	    if (o != null) {
		if (o.equals("false")) {
		    i = 0;
		} else if (o.equals("true")) {
		    i = 1;
		} else {
		    i = Integer.parseInt(o.toString());
		}
	    }
	} catch (NumberFormatException e) {
	}
	return i;
    }

    public static Double toDbl(Object o) {
	return toDbl(o, 0.0);
    }

    public static Double toDbl(Object o, Double defaultValue) {
	Double i = defaultValue;
	try {
	    if (o != null && !Util.toString(o).equals("")) {
		i = Double.parseDouble(o.toString());
	    }
	} catch (NumberFormatException e) {
	}
	return i;
    }

    public static Float toFloat(Object o) {
	return toFloat(o, (float) 0.0);
    }

    public static Float toFloat(Object o, Float defaultValue) {
	Float i = defaultValue;
	try {
	    if (o != null && !Util.toString(o).equals("")) {
		i = Float.parseFloat(o.toString());
	    }
	} catch (NumberFormatException e) {
	}
	return i;
    }

    public static Short toShort(Object o) {
	return toShort(o, Short.parseShort("0"));
    }

    public static Short toShort(Object o, Short defaultValue) {
	Short i = defaultValue;
	try {
	    if (o != null) {
		i = Short.parseShort(o.toString());
	    }
	} catch (NumberFormatException e) {
	}
	return i;
    }

    public static Long toLong(Object o) {
	return toLong(o, Long.parseLong("0"));
    }

    public static Long toLong(Object o, Long defaultValue) {
	Long i = defaultValue;
	try {
	    if (o != null) {
		i = Long.parseLong(o.toString());
	    }
	} catch (NumberFormatException e) {
	}
	return i;
    }

    /**
     * 精确相除
     * 
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static float divide(Object a, Object b, int scale) {
	try {
	    BigDecimal b1 = new BigDecimal(a.toString());
	    BigDecimal b2 = new BigDecimal(b.toString());
	    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	} catch (Exception e) {
	    return 0;
	}
    }

    public static String toDateStr(Timestamp o) {
	return toDateStr(o, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toShortDateStr(Timestamp o) {
	return toDateStr(o, "yyyy-MM-dd");
    }

    public static String toDateStr(Timestamp ts, String formatStr) {
	String str = "";
	SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
	formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	ParsePosition pos = new ParsePosition(0);
	Date strtodate = formatter.parse(Util.toString(ts), pos);
	if (strtodate != null) {
	    str = formatter.format(strtodate);
	}
	return str;
    }

    public static boolean toBoolean(Object o) {
	return toBoolean(o, false);
    }

    public static boolean toBoolean(Object o, boolean bool) {
	try {
	    if (toString(o).equals("1") || toString(o).equals("true")) {
		return true;
	    } else {
		return false;
	    }
	} catch (NumberFormatException e) {
	}
	return bool;
    }

    /**
     * 字符串截取substring
     * 
     * @param str
     * @param length
     *            从左到右的长度
     * @return
     */
    public static String substring(String str, int length) {
	return substring(str, length, "");
    }

    public static String substring(String str, int length, String suffix) {
	return substring(str, 0, length, suffix);
    }

    public static String subLaststring(String str, int length) {
	str = Util.toString(str);
	int beginIndex = str.length() - length;
	return substring(str, beginIndex, length, "");
    }

    public static String substring(String str, int beginIndex, int length, String suffix) {
	str = Util.toString(str);
	if (str.length() >= length && length > 0) {
	    if (beginIndex == 0) {
		str = str.substring(beginIndex, length - suffix.length()) + suffix;
	    } else {
		str = str.substring(beginIndex);
	    }
	}
	return str;
    }

    public static String keepLen(String str, int len) {
	return keepLen(str, len, "　");
    }

    /**
     * 保持字符串定长
     * 
     * @param str
     * @param len
     * @param prefix
     * @return
     */
    public static String keepLen(String str, int len, String prefix) {
	str = toString(str);
	int fill = len - str.length();
	if (fill <= 0) {
	    str = substring(str, len);
	} else {
	    for (int i = 0; i < fill; i++) {
		str = prefix + str;
	    }
	}
	return str;
    }

    /**
     * 获得当前时间
     * 
     * @return
     */
    public static int getTimestamp_int() {
	return Util.toInt(new Timestamp(new Date().getTime()).getTime());
    }

    public static Timestamp getTimestamp() {
	return new Timestamp(new Date().getTime());
    }

    /**
     * 去除字符串两个头尾之间的字符,比如去除html的特殊符号
     * 
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static String trimIn(String str, String begin, String end) {
	if (str == null) {
	    return str;
	}
	int beginIndex = str.indexOf(begin);
	int endIndex = str.indexOf(end);
	if (beginIndex >= 0 && endIndex >= 0 && endIndex > beginIndex) {
	    str = str.substring(0, beginIndex) + str.substring(endIndex + 1, str.length());
	    return trimIn(str, begin, end);
	} else {
	    return str;
	}
    }

    /**
     * 去除字符串两个头尾之外的字符
     * 
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static String trim(String str, String begin, String end) {
	if (str == null) {
	    return str;
	}
	int beginIndex = str.indexOf(begin);
	int endIndex = str.indexOf(end);
	if (beginIndex >= 0 && endIndex >= 0) {
	    str = str.substring(beginIndex + 1, endIndex);
	}
	return str;
    }

    private Util() {
    }


    /**
     * 获取数字随机数
     * 
     * @param size
     *            随机数个数
     * @return 指定个数的数字随机数的字符串
     */
    public static String getRandom(int size) {
	Random random = new Random();
	StringBuilder sb = new StringBuilder(size);
	for (int i = 0; i < size; i++) {
	    sb.append(random.nextInt(9));
	}
	return sb.toString();
    }

    /**
     * 根据随即码以及宽高绘制图片函数
     * 
     * @param g
     *            Graphics
     * @param width
     *            图片宽度
     * @param height
     *            图片高度
     * @param randomCode
     *            随机码
     */
    public static void drawRandomPicture(Graphics g, int width, int height, String randomCode) {
	g.setColor(randColor(200, 250));
	g.fillRect(0, 0, width, height);
	g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	g.setColor(randColor(160, 200));

	Random random = new Random(System.currentTimeMillis());

	//  随机产生155条干扰线，使图像中的验证码不易被识别
	for (int i = 0; i < 155; i++) {
	    int x = random.nextInt(width);
	    int y = random.nextInt(height);
	    int xl = random.nextInt(12);
	    int yl = random.nextInt(12);
	    g.drawLine(x, y, x + xl, y + yl);
	}

	//  将验证码显示在图像中
	for (int i = 0; i < 4; i++) {
	    g.setColor(randColor(20, 130));
	    g.drawString(randomCode.substring(i, i + 1), 13 * i + 6, 16);
	}
    }

    /**
     * 获得随机色
     * 
     * @param fc
     *            前景色
     * @param bc
     *            背景色
     * @return 随机色
     */
    private static Color randColor(int fc, int bc) {
	Random random = new Random(System.currentTimeMillis());
	if (fc > 255)
	    fc = 255;
	if (bc > 255)
	    bc = 255;
	int r = fc + random.nextInt(bc - fc);
	int g = fc + random.nextInt(bc - fc);
	int b = fc + random.nextInt(bc - fc);
	return new Color(r, g, b);
    }

    /**
     * Url特殊字符转义
     * 
     * @param str
     * @return
     */
    public static String Url_replace(String str) {
	str = str.replace("%", "%25");
	str = str.replace("+", "%2B");
	str = str.replace("/", "%2F");
	str = str.replace("?", "%3F");
	str = str.replace("#", "%23");
	str = str.replace("&", "%26");
	str = str.replace("-", "%2D");
	str = str.replace(" ", "%20");
	return str;
    }

    /**
     * 将源字符串使用MD5加密为字节数组
     * @param source
     * @return
     */
    public static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF-8"));
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
         
        return result;
    }
     
    /**
     * 将源字符串使用MD5加密为32位16进制数
     * @param source
     * @return
     */
    public static String encode2hex(String source) {
        byte[] data = encode2bytes(source);
 
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);
             
            if (hex.length() == 1) {
                hexString.append('0');
            }
             
            hexString.append(hex);
        }
         
        return hexString.toString();
    }
     
    /**
     * 验证字符串是否匹配
     * @param unknown 待验证的字符串
     * @param okHex 使用MD5加密过的16进制字符串
     * @return  匹配返回true，不匹配返回false
     */
    public static boolean validate(String unknown , String okHex) {
        return okHex.equals(encode2hex(unknown));
    }
    
    /**
     * 字符串转换成list
     * 
     * @param str
     * @param split
     * @return
     */
    public static List<String> string2List(String str, String split) {
		List<String> list = new ArrayList<String>();
		if (!Util.isEmpty(str)) {
		    for (String s : Util.toString(str).split(split)) {
			list.add(s);
		    }
		}
		return list;
    }
    
    /**
     * 判断是否存在特定字符
     * 
     * @param str
     * @param regEx
     * @return
     */
    public static boolean isRegEx(String str, String regEx) {
    	regEx = "[" + regEx + "]";
    	Pattern p = Pattern.compile(regEx);
    	Matcher m = p.matcher(str);
		return m.find();
    }

    public static List<String> string2List(String str) {
    	return string2List(str, ",");
    }

	//图片转化成base64字符串
	public static String GetImageStr(String imgFile) throws BizException
	{//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//		String imgFile = "D:\\360CloudUI\\tupian\\jt.jpg";//待处理的图片
		FileInputStream fileInputStream = null;
		byte[] data = null;
		//读取图片字节数组
		try
		{
			File file = new File(imgFile);
			if(!file.exists()){
				throw new BizException("文件不存在");
			}
			long fileSize = file.length();
			fileInputStream = new FileInputStream(file);
			data = new byte[(int) fileSize];

			int offset = 0;
			int numRead = 0;
			while (offset < data.length && (numRead = fileInputStream.read(data, offset, data.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset != data.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			fileInputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//对字节数组Base64编码
		String base64Str = org.apache.shiro.codec.Base64.encodeToString(data);
		return base64Str;//返回Base64编码过的字节数组字符串
	}

	public static byte[] GetImageData(String imgFile) throws BizException,IOException
	{//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//		String imgFile = "D:\\360CloudUI\\tupian\\jt.jpg";//待处理的图片
		FileInputStream fileInputStream = null;
		byte[] data = null;
		//读取图片字节数组
		File file = new File(imgFile);
		if(!file.exists()){
			throw new BizException("文件不存在，路径：" + imgFile);
		}
		long fileSize = file.length();
		fileInputStream = new FileInputStream(file);
		data = new byte[(int) fileSize];

		int offset = 0;
		int numRead = 0;
		while (offset < data.length && (numRead = fileInputStream.read(data, offset, data.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset != data.length) {
			throw new BizException("无法完整读取图片，路径：" + imgFile);
		}
		fileInputStream.close();

		return data;
	}

//	public static void main(String argus[]) throws IOException {
//		OutputStream os;
//		String str = GetImageStr("C:\\Users\\宏炜\\Desktop\\67230177577958683.jpg");
//		System.out.println(str);
//
//		byte[] data = Base64.decode(str);
//
//		File file = new File("E:\\test.jpg");
//		if(!file.exists()){
//			file.mkdir();
//		}
//		os = new FileOutputStream(file.getPath() + File.separator + "test.jpg");
//		os.write(data, 0, data.length);
//	}

}
