package yx.pay.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.domain.wx.WxConfig;

@Slf4j
@Component
public class WxHttpsUtil {

    private SSLConnectionSocketFactory sslsf = null;

    private void initSSLConnectionSocketFactory(String certPath, String password) {
        synchronized(this) {
            if(sslsf != null) {
                return;
            }
            FileInputStream fis = null;
            try {
                // 指定读取证书格式为PKCS12
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                // 读取本机存放的PKCS12证书文件
                fis = new FileInputStream(new File(certPath));
                // 指定PKCS12的密码(商户ID)
                char[] pwds = password.toCharArray();
                keyStore.load(fis, pwds);
                // 下载证书时的密码、默认密码是你的MCHID mch_id
                SSLContext sslcontext = SSLContexts.custom()
                        .loadKeyMaterial(keyStore, pwds)// 这里也是写密码的
                        .build();
                // Allow TLSv1 protocol only
                sslsf = new SSLConnectionSocketFactory(
                        sslcontext, new String[] { "TLSv1" }, null,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            } catch (KeyStoreException e) {
                log.error("",e);
            } catch (FileNotFoundException e) {
                log.error("",e);
            } catch (NoSuchAlgorithmException e) {
                log.error("",e);
            } catch (CertificateException e) {
                log.error("",e);
            } catch (IOException e) {
                log.error("",e);
            } catch (KeyManagementException e) {
                log.error("",e);
            } catch (UnrecoverableKeyException e) {
                log.error("",e);
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        log.error("",e);
                    }
                }
            }
        }
    }

    public String call(String url, String requestData, MerchantServerConfig merchantServerConfig) {
        // 商户id
        String mchId = merchantServerConfig.getMerchantId();
        // 证书路径
        String certPath = merchantServerConfig.getCertPath();
        //初始化SSLConnectionSocketFactory
        if(sslsf == null) {
            initSSLConnectionSocketFactory(certPath, mchId);
        }
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        CloseableHttpResponse response  = null;
        String responseData = null;
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(requestData, "UTF-8"));
            log.info("请求微信的数据："+requestData);
            response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            responseData = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(entity);
            log.info("微信返回的数据："+responseData);
        } catch (ClientProtocolException e) {
            log.error("",e);
        } catch (IOException e) {
            log.error("",e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("",e);
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("",e);
            }
        }
        return responseData;
    }
}
