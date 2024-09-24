package com.inspur.ucif.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 广东政法委用户体系前面算法
 * @author liyunlong
 * @date 2024-05-30
 * */
public class GdzfwHAUtils {
	public static final String ENCODE_TYPE_HMAC_SHA_256 ="HmacSHA256";
    
	/**
	  * 签名
	 * @param secret  签名secret
	 * @param message 签名字符串
	 * @return
	 */
    public static String encode(String secret,String message){
        String encodeStr = null;
        if (StringUtils.isEmpty(secret)){
            return null;
        }
        try{
            //1.HMAC_SHA256 加密
            Mac HMAC_SHA256 = Mac.getInstance(ENCODE_TYPE_HMAC_SHA_256);
            SecretKeySpec secre_spec = new SecretKeySpec(secret.getBytes("UTF-8"),ENCODE_TYPE_HMAC_SHA_256);
            HMAC_SHA256.init(secre_spec);
            byte[] bytes = HMAC_SHA256.doFinal(message.getBytes("UTF-8"));
            if (bytes==null || bytes.length<1){
                return null;
            }
            //2.转换为16进制字符串
            String SHA256 =byteToHex(bytes);
            if (StringUtils.isEmpty(SHA256)){
                return null;
            }
            //3.BASE64
            encodeStr = Base64.getEncoder().encodeToString(SHA256.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
       return encodeStr;
    }
    /**
          * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byteToHex(byte[] bytes){
        if (bytes==null){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String temp=null;
        for (int i = 0; i <bytes.length ; i++) {
            temp = Integer.toHexString(bytes[i]&0xff);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
