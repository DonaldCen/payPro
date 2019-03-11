package yx.pay.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import yx.pay.system.domain.wx.WxConfig;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Component
public class WxUtil {
    // 获取token接口(GET)
    public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // oauth2授权接口(GET)
    public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

    /**
     * 微信支付接口地址
     */
    // 微信支付统一接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";

    private static final SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private WxConfig wxConfig;

    public void commonParams(SortedMap<Object, Object> packageParams) {
        // 账号信息
        String appId = wxConfig.getAppId(); // appid
        String mchId = wxConfig.getMchId(); // 商业号
        String strRandom = String.valueOf(buildRandom(4)) ;
        // 生成随机字符串
        String currTime = getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String nonce_str = strTime + strRandom;
        packageParams.put("appid", appId);// 公众账号ID
        packageParams.put("mch_id", mchId);// 商户号
        packageParams.put("nonce_str", nonce_str);// 随机字符串
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public String getCurrTime() {
        Date now = new Date();
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     * @param length
     * @return
     */
    public int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    public String createSign(String characterEncoding, SortedMap<Object, Object> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wxConfig.getAppId());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    public String buildQrCodeInfo(SortedMap<Object, Object> packageParams,String sign){
        StringBuffer qrCode = new StringBuffer();
        qrCode.append("weixin://wxpay/bizpayurl?");
        qrCode.append("appid="+wxConfig.getAppId());
        qrCode.append("&mch_id="+wxConfig.getMchId());
        qrCode.append("&nonce_str="+packageParams.get("nonce_str"));
        qrCode.append("&product_id="+packageParams.get("product_id"));
        qrCode.append("&time_stamp="+packageParams.get("time_stamp"));
        qrCode.append("&sign="+sign);
        return qrCode.toString();
    }
}
