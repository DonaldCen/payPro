package yx.pay.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by 0151717 on 2019/3/23.
 */
@Slf4j
public class SignUtil {

    /**
     * 微信证书HMAC-SHA256签名
     *
     * @param params
     * @param secret
     * @return
     */
    public static String wechatCertficatesSignBySHA256(Map<String, String> params, String secret) {
        // 需要保证排序
        SortedMap<String, String> sortedMap = new TreeMap<>(params);
        // 将参数拼接成字符串
        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }
      //  toSign.append("key=").append(secret);//添加商户Key
        log.info("参数排序后的参数串 {}",toSign.toString());
       // return sha256_HMAC(toSign.toString(), secret);
        return  toSign(new StringBuffer(toSign.toString()),secret ,"HMAC-SHA256");
//        return  toSign(new StringBuffer(toSign.toString()),secret ,"MD5");
    }



    /**
     * 加密HMAC-SHA256
     *
     * @param message
     * @param secret
     * @return
     */
    private static String sha256_HMAC_ZB(String message, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            //对消息进行UTF-8转化  为了防止中文加密与微信的算法不匹配
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
            String sign = byteArrayToHexString(bytes);
            log.info("HMAC-SHA256格式加密后台的Sign {}", sign.toString());
            sign = sign.toUpperCase();
            log.info("转成大写串的Sign {}", sign.toString());
            return sign;
        } catch (Exception e) {
            log.error("sha256_HMAC加密异常", e);
        }
        return null;
    }

    /**
     * 加密后的字节转字符串
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = null;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }


    /**
     * 生成签名
     * @param str         要签名的字段
     * @param signType    签名类型
     * @param key    商户在公众平台的唯一KEY 或  "商户的唯一KEY"
     * @return
     */
    public static String toSign(StringBuffer str, String key ,String signType) {
        StringBuffer sb = str.append("key=").append(key);
        String sign = null;
        sign = new String(sb.toString());
        log.info("生成对象成功: {}", sign);
        if (null==signType || "MD5".equals(signType)) {// 签名类型为空，则默认为MD5
            return MD5Encode(sign).toUpperCase();
        }
        return sha256_HMAC(sign, key).toUpperCase();
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            //MD5也要加密
            md.update(resultString.getBytes("UTF-8"));
            resultString = byteArrayToHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("MD5 生成结果：" + resultString);
        return resultString;
    }
    /**
     * sha256_HMAC加密
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            //对消息进行UTF-8转化  为了防止中文加密与微信的算法不匹配
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
            hash = byteArrayToHexString(bytes);
            log.info(hash);
        } catch (Exception e) {
            log.info("Error HmacSHA256 ===========" + e.getMessage());
        }
        log.info("Sha256 生成结果：" + hash);
        return hash;
    }
}