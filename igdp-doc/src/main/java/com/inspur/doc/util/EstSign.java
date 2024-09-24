package com.inspur.doc.util;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName EstSign
 * @date 2024/8/2 16:31
 */
public class EstSign {
    private static String hmacsha = "HmacSHA256";
    private static String charSet = "utf-8";

    public EstSign() {
    }


    public static String sign(String key, String data) {
        if (data != null && !Objects.isNull(key) && !key.isEmpty()) {
            try {
                return sign(key, (InputStream) (new ByteArrayInputStream(data.getBytes(charSet))));
            } catch (UnsupportedEncodingException var4) {
                UnsupportedEncodingException e = var4;
                throw new RuntimeException("计算签名出错了", e);
            }
        } else {
            throw new RuntimeException("参数不能为空空");
        }
    }

    private static String sign(String key, InputStream data) {
        String ak = key;

        try {
            Mac mac = Mac.getInstance(hmacsha);
            byte[] secretByte = ak.getBytes(charSet);
            SecretKey secret = new SecretKeySpec(secretByte, hmacsha);
            mac.init(secret);
            byte[] b = new byte[1024];

            int tlen;
            while ((tlen = data.read(b)) != -1) {
                mac.update(b, 0, tlen);
            }

            byte[] doFinal = mac.doFinal();
            String checksum = bytesToHex(doFinal);
            return checksum;
        } catch (InvalidKeyException | IllegalStateException | IOException | NoSuchAlgorithmException var11) {
            Exception e = var11;
            throw new RuntimeException("计算签名出错了", e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    public boolean validate(String key, String data, String sign) {
        String calcSign = this.sign(key, data);
        return calcSign != null && calcSign.equals(sign);
    }
}
