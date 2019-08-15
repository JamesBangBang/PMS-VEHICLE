package com.starnetsecurity.common.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

/**
 *@Author cf
 *@Date 2018/9/9 16:11
 *@Description 专门用于解析和加密各个子系统之间通用的登录token
 */
public class TokenUtil {
    public static final String aesKey = "75e82e251b7896be654f4080668b8e06";

    public static String encrypt(String msg){
        AesCipherService aesCipherService = new AesCipherService();
        return aesCipherService.encrypt(msg.getBytes(), org.apache.shiro.codec.Hex.decode(aesKey)).toHex();
    }

    public static String decrypt(String msg){
        AesCipherService aesCipherService = new AesCipherService();
        return new String(aesCipherService.decrypt(org.apache.shiro.codec.Hex.decode(msg), org.apache.shiro.codec.Hex.decode(aesKey)).getBytes());
    }

    public static String decodeToken(String accessToken){
        accessToken = accessToken.replaceAll("-", "+").replaceAll("\\*", "/");
        AesCipherService aesCipherService = new AesCipherService();
        ByteSource byteSource = aesCipherService.decrypt(Base64.decode(accessToken), org.apache.shiro.codec.Hex.decode(aesKey));
        accessToken = new String(byteSource.getBytes());
        return accessToken;
    }

    public static String encodeToken(String accessToken) {
        AesCipherService aesCipherService = new AesCipherService();
        String encodeToken = aesCipherService.encrypt(accessToken.getBytes(), org.apache.shiro.codec.Hex.decode(aesKey)).toBase64().replaceAll("\\+", "-").replaceAll("/", "*");
        return encodeToken;
    }

    public static void main(String[] args) {
//        String str = "{\"loginTime\":1536388160862,\"operatorUserId\":\"123456789\",\"password\":\"123456\",\"rememberMe\":false,\"serverList\":null,\"username\":\"root\"}";
//        String encodeToken = "4DmGir*cUJ94bGa30ldd2J3vi7u6JjQusGS8MdBzWH8lhbvWnV9BFl84KWWDzdxCuiLwCBekb9Y--PrF1wjqJQJdoTkBGoBaPdNc6Anw8WBLZqKdrCXlDP11EHmyhkPW-bB8TjHLkUByQxxgPRpQZ1AHc6KBdfFK6icXIOL6Ozg=";
//        String deToken = decodeToken(encodeToken);
//        System.out.println(deToken);
        String strHex = "75e82e251b7896be654f4080668b8e06";
        Long longHex = Long.parseLong(strHex, 16);
        System.out.println(longHex.toString().length());
    }
}
