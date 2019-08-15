package com.starnetsecurity.common.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

/**
 * Created by 宏炜 on 2018-01-04.
 */
public class AesUtil {

    public static final String aesKey = "75e82e251b7896be654f4080668b8e06";

    public static String encrypt(String msg){
        AesCipherService aesCipherService = new AesCipherService();
        return aesCipherService.encrypt(msg.getBytes(), Hex.decode(aesKey)).toHex();
    }

    public static String decrypt(String msg){
        AesCipherService aesCipherService = new AesCipherService();
        return new String(aesCipherService.decrypt(Hex.decode(msg),Hex.decode(aesKey)).getBytes());
    }

    public static String decodeToken(String accessToken){
        accessToken = accessToken.replaceAll("-", "+").replaceAll("\\*", "/");
        AesCipherService aesCipherService = new AesCipherService();
        ByteSource byteSource = aesCipherService.decrypt(Base64.decode(accessToken),Hex.decode(AesUtil.aesKey));
        accessToken = new String(byteSource.getBytes());
        return accessToken;
    }

    public static void main(String[] argus){
        String msg = "123456789";
        System.out.println(msg);
        String aesStr = encrypt(msg);
        System.out.println(aesStr);
        System.out.println(decrypt(aesStr));

    }
}
