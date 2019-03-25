package yx.pay.system.service.impl;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.wx.MerchantMapper;
import yx.pay.system.domain.wx.Merchant;

import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.service.MerchantService;

@Slf4j
@Service
public class MerchantServiceImpl extends BaseService<Merchant> implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    public MerchantServerConfig merchantServerConfig;
    @Override
    public void updateMerchantQrUrl(Merchant merchant) {
        merchantMapper.updateMerchantQrUrl(merchant);
    }

    @Override
    public List<Merchant> findMerchantList(QueryRequest request, Merchant merchant) {
        try {
            if (request.getSortField() != null) {
                merchant.setSortField(request.getSortField());
                if (StringUtils.equals(FebsConstant.ORDER_ASC, request.getSortOrder()))
                    merchant.setSortOrder("asc");
                else if (StringUtils.equals(FebsConstant.ORDER_DESC, request.getSortOrder()))
                    merchant.setSortOrder("desc");
            }
            return this.merchantMapper.findMerchantList(merchant);
        } catch (Exception e) {
            log.error("查询商户异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Merchant findByName(String merchantName) {
        return this.merchantMapper.findByName(merchantName);
    }

    @Override
    public void addMerchant(@Valid Merchant merchant) {
        this.merchantMapper.addMerchant(merchant);
    }

    @Override
    public void updateMerchant(@Valid Merchant merchant) {
        this.merchantMapper.updateMerchant(merchant);
    }


    public String MerchantApply(QueryRequest request, Map<String,String> paramMap){
        // 初始化一个HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Post请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/applyment/micro/submit");
        try {
//            String signStr = WXPayUtil.generateSignature(param, merchantServerConfig.getApiKey(), SignType.HMACSHA256);
            String signStr = WXPayUtil.generateSignature(paramMap, merchantServerConfig.getApiKey(), WXPayConstants.SignType.MD5);
            paramMap.put("sign",signStr);
            log.info("签名"+signStr);
//            String xmlData=WXPayUtil.generateSignedXml(param, merchantServerConfig.getApiKey(),SignType.HMACSHA256);
            String xmlData=WXPayUtil.generateSignedXml(paramMap, merchantServerConfig.getApiKey(), WXPayConstants.SignType.MD5);
            log.info("xml 格式串 {}",xmlData);
            boolean flag=WXPayUtil.isSignatureValid(xmlData,merchantServerConfig.getApiKey());
            log.info("签名验证标识"+flag);
            httpPost.setEntity(new StringEntity(xmlData, "UTF-8"));
        }catch(Exception e){
            log.info("签名异常 {}" ,e.getMessage());
        }
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.getMimeType());
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            log.info("商户入驻请求响应 {}", httpResponse);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseEntity = EntityUtils.toString(httpResponse.getEntity());
                Document document = DocumentHelper.parseText(responseEntity);
                log.info("document->"+document.toString());

                if ("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//return_code").getStringValue())
                        && "SUCCESS".equalsIgnoreCase(document.selectSingleNode("//result_code").getStringValue())) {
                    //log.info("获取平台证书 {}",document.selectSingleNode("//certificates").getStringValue());
                    return document.selectSingleNode("//applyment_id").getStringValue();
                }else{
                    log.error("商户入驻请求响应异常 {}", document.selectSingleNode("//return_msg").getStringValue());
                    if("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//return_code").getStringValue())){
                        log.info("1、随机字符串 {}",document.selectSingleNode("//nonce_str").getStringValue());
                        log.info("2、签名 {}",document.selectSingleNode("//sign").getStringValue());
                        log.info("3、业务结果 {}",document.selectSingleNode("//result_code").getStringValue());
                        log.info("4、错误代码 {}",document.selectSingleNode("//err_code").getStringValue());
                        log.info("5、错误代码描述 {}",document.selectSingleNode("//err_code_des").getStringValue());
                        log.info("6、参数校验不通过的字段名 {}",document.selectSingleNode("//err_param").getStringValue());

                    }
                }


            }
        } catch (Exception e) {
            log.error("执行httpclient商户入驻请求错误 {}", e);
        }
        return null;
    }
}
