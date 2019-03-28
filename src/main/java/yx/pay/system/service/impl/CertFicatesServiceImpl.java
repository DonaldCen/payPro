package yx.pay.system.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import yx.pay.common.utils.EncryptionUtils;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.service.CertFicatesService;
import yx.pay.common.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Service;

import  com.github.wxpay.sdk.WXPayUtil;
import  com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;

import java.util.*;

/**
 * Created by 0151717 on 2019/3/23.
 */
@Service
@Slf4j
public class CertFicatesServiceImpl implements CertFicatesService {
    @Autowired
    public MerchantServerConfig merchantServerConfig;
    @Override
    public String getCertFicates() {
        // 初始化一个HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Post请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/risk/getcertficates");
        /**
         * 这边需要您提供微信分配的商户号跟API密钥
         */
        Map<String, String> param = new HashMap<>(4);
        param.put("mch_id", merchantServerConfig.getMerchantId());
        //随机串
        param.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        // 暂只支持HMAC-SHA256 加密
        param.put("sign_type", "HMAC-SHA256");
        // 对你的参数进行加密处理
        //param.put("sign", SignUtil.wechatCertficatesSignBySHA256(param, "API密钥(mch_key)" ));
        try {
            String signStr = WXPayUtil.generateSignature(param, merchantServerConfig.getApiKey(), SignType.HMACSHA256);
            param.put("sign",signStr);
            log.info("签名"+signStr);
            String xmlData=WXPayUtil.generateSignedXml(param, merchantServerConfig.getApiKey(),SignType.HMACSHA256);
            log.info("xml 格式串 {}",xmlData);
            boolean flag=WXPayUtil.isSignatureValid(xmlData,merchantServerConfig.getApiKey());
            log.info("签名验证标识"+flag);
            httpPost.setEntity(new StringEntity(xmlData, "UTF-8"));
        }catch(Exception e){
            log.info("签名异常 {}" ,e.getMessage());
        }
//        httpPost.setEntity(new StringEntity(map2Xml(param), "UTF-8"));
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.getMimeType());
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            log.info("获取平台证书响应 {}", httpResponse);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseEntity = EntityUtils.toString(httpResponse.getEntity());
                Document document = DocumentHelper.parseText(responseEntity);
                log.info("document->"+document.toString());
                if ("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//return_code").getStringValue())
                        && "SUCCESS".equalsIgnoreCase(document.selectSingleNode("//result_code").getStringValue())) {
                    log.info("获取平台证书 {}",document.selectSingleNode("//certificates").getStringValue());
                    return document.selectSingleNode("//certificates").getStringValue();
                }
                log.error("请求平台证书序号响应异常 {}", document.selectSingleNode("//return_msg").getStringValue());
            }
        } catch (Exception e) {
            log.error("执行httpclient请求平台证书序号错误 {}", e);
        }
        return null;
    }
    /**
     * map对象转xml
     *
     * @param map
     * @return
     */
    private String map2Xml(Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        // 需要保证排序
        SortedMap<String, String> sortedMap = new TreeMap<>(map);
        result.append("<xml>");
        StringBuilder signXML=new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = map.get(key);
            if("sign".equals(key)) {
                signXML.append("<" + key + ">");
                signXML.append(value);
                signXML.append("</" + key + ">");

            }else {
                result.append("<" + key + "><![CDATA[");
                result.append(value);
                result.append("]]></" + key + ">");

            }
        }
        //  不是signxml
        result.append(signXML.toString());
        /*
        StringBuilder result = new StringBuilder();
        result.append("<xml>");
        if (map != null && map.keySet().size() > 0) {
            map.forEach((key, value) -> {
                if( "sign".equals(key)){
                    result.append("<" + key + ">");
                    result.append(value);
                    result.append("</" + key + ">");
            }else{
                result.append("<" + key + "><![CDATA[");
                // result.append("<" + key + ">");
                result.append(value);
                result.append("]]></" + key + ">");
                // result.append("</" + key + ">");
            }
        });

        }
           */
        result.append("</xml>");
        log.info("XML封装后的参数 {}",result.toString());
        return result.toString();
    }

    @Override
    public String decryptCertSN(String associatedData, String nonce, String cipherText, String apiv3Key) throws Exception{
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(apiv3Key.getBytes(), "AES");
        GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(associatedData.getBytes());
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

    /**
     * 敏感内容加密，获取对应的密文
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public String encryptPkcs1padding(String content)throws Exception{
        return EncryptionUtils.rsaEncrypt(content,merchantServerConfig.getCertPath() );

    }
}