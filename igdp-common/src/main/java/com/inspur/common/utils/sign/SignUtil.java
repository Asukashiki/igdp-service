package com.inspur.common.utils.sign;

import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.MD5;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SignUtil
 * @date 2024/6/14 14:44
 */
public class SignUtil {

    /**
     * 签名算法
     * 1. 计算步骤
     * 用于计算签名的参数在不同接口之间会有差异，但算法过程固定如下4个步骤。
     * 将<key, value>请求参数对按key进行字典升序排序，得到有序的参数对列表N
     * 将列表N中的参数对按URL键值对的格式拼接成字符串，得到字符串T（如：key1=value1&key2=value2），URL键值拼接过程value部分需要URL编码，URL编码算法用大写字母，例如%E8，而不是小写%e8
     * 将应用密钥以secret_key为键名，组成URL键值拼接到字符串T末尾，得到字符串S（如：key1=value1&key2=value2&secret_key=密钥)
     * 对字符串S进行MD5运算，将得到的MD5值所有字符转换成大写，得到接口请求签名
     * 2. 注意事项
     * 不同接口要求的参数对不一样，计算签名使用的参数对也不一样
     * 参数名区分大小写，参数值为空不参与签名
     * URL键值拼接过程value部分需要URL编码
     * @return 签名字符串
     */
    private static String getSign(Map<String, Object> map, String secretKey) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> m : infoIds) {
            if(null == m.getValue() || StringUtils.isNotBlank(m.getValue().toString())){
                sb.append(m.getKey()).append("=").append(URLUtil.encodeAll(m.getValue().toString())).append("&");
            }
        }
        sb.append("secret_key").append(secretKey);
        return MD5.create().digestHex(sb.toString()).toUpperCase();
    }


    /**签名验证方法*/
    public static boolean signValidate(Map<String, Object> map,String secretKey,String sign){
        String mySign = getSign(map,secretKey);
        return mySign.equals(sign);
    }
}

