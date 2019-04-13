package yx.pay.common.wechat.kit;

import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.AccessToken;
import com.jfinal.weixin.sdk.kit.ParaMap;
import com.jfinal.weixin.sdk.utils.RetryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yx.pay.system.domain.wx.WxConfig;

import java.util.Map;
import java.util.concurrent.Callable;

@Component
public class AccessTokenUtil {

    private static final String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    @Autowired
    private WxConfig config;

    public synchronized String getAccessToken() {
        String appId = config.getAppId();
        String appSecret = config.getAppSecret();
        final Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", appSecret).getData();
        // 最多三次请求
        AccessToken result = RetryUtils.retryOnException(3, new Callable<AccessToken>() {

            @Override
            public AccessToken call() throws Exception {
                String json = HttpKit.get(URL, queryParas);
                return new AccessToken(json);
            }
        });
        return result.getAccessToken();
    }
}
