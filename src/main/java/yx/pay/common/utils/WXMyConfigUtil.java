package yx.pay.common.utils;


import com.github.wxpay.sdk.WXPayConfig;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by 0151717 on 2019/3/24.
 */
public class WXMyConfigUtil implements WXPayConfig {
    private byte[] certData;
    public WXMyConfigUtil() throws Exception {
        String certPath = "apiclient_cert.p12";//从微信商户平台下载的安全证书存放的目录
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return "你微信账户的appid";
    }

    //parnerid
    @Override
    public String getMchID() {
        return "你微信账户的商户id";
    }

    @Override
    public String getKey() {
        return "你微信账户的api密钥";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}