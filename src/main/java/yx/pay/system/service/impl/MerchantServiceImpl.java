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

import java.lang.reflect.Field;
import java.util.*;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.impl.BaseService;
import yx.pay.common.utils.CertficateParseUtil;
import yx.pay.common.utils.EncryptionUtils;
import yx.pay.common.utils.SSLContextUtils;
import yx.pay.common.utils.SignUtil;
import yx.pay.system.dao.BankMapper;
import yx.pay.system.dao.RateMapper;
import yx.pay.system.dao.wx.MerchantApplyMapper;
import yx.pay.system.dao.wx.MerchantMapper;
import yx.pay.system.domain.Rate;
import yx.pay.system.domain.vo.MerchantRegisterVo;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.service.BankService;
import yx.pay.system.service.CertFicatesService;
import yx.pay.system.service.MerchantService;
import yx.pay.system.service.RateService;

@Slf4j
@Service
public class MerchantServiceImpl extends BaseService<Merchant> implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private BankService bankService;
    @Autowired
    private RateService rateService;
    @Autowired
    private MerchantServerConfig merchantServerConfig;

    @Autowired
    private CertFicatesService certFicatesService;
    @Autowired
    private MerchantApplyMapper merchantApplyMapper;
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

    /**
     * 只接收HMAC-SHA256  签名
     * @param vo
     * @return
     */
    public FebsResponse merchantApply(MerchantRegisterVo vo){
        // 数据库参数和加密参数Map
        Map<String,String>[] params =merchantRegisterToMap(vo);
//        return null;
        // 初始化一个HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Post请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/applyment/micro/submit");
        try {
            httpClient = HttpClients.custom().setSSLContext(SSLContextUtils.getSSLContext(merchantServerConfig.getCertPath(), merchantServerConfig.getMerchantId())).build();
            String signStr = SignUtil.wechatCertficatesSignBySHA256(params[1], merchantServerConfig.getApiKey());//WXPayUtil.generateSignature(params[1], merchantServerConfig.getApiKey(), WXPayConstants.SignType.HMACSHA256);
            params[0].put("sign",signStr);
            params[1].put("sign",signStr);
            log.info("商户入驻申请接口签名 "+signStr);
            String xmlData=WXPayUtil.generateSignedXml(params[1], merchantServerConfig.getApiKey(), WXPayConstants.SignType.HMACSHA256);
            log.info("xml 格式串 {}",xmlData);
            boolean flag=WXPayUtil.isSignatureValid(xmlData,merchantServerConfig.getApiKey());
            log.info("签名验证标识"+flag);
            httpPost.setEntity(new StringEntity(xmlData, "UTF-8"));
        }catch(Exception e){
            e.printStackTrace();
            log.info("签名异常 {}" ,e.getMessage());
        }

        checkParam( params[1]);
        // 先保存申请数据到数据库中(待处理:状态 申请中)
        params[0].put("status","申请入驻中");
        Long id=merchantApplyMapper.getMerchantApplyNextID();
        params[0].put("id",id.toString());
        params[1].put("id",id.toString());
        params[0].put("data_status","1");//初始失效状态，成功则会修改为生效状态
        // 申请参数入库
        merchantApplyMapper.addMerchantApply(params[0]);
      //  if(params.length==2) return new FebsResponse().fail("开关--入驻请求失败 测试数据 ");

        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.getMimeType());
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            log.info("商户入驻请求响应 {}", httpResponse);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseEntity = EntityUtils.toString(httpResponse.getEntity());
                Document document = DocumentHelper.parseText(responseEntity);
                log.info("document->"+document.toString());
                if ("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//return_code").getStringValue())){
                    log.error("商户入驻请求响应异常 {}", document.selectSingleNode("//return_msg").getStringValue());
                    if ("FAIL".equalsIgnoreCase(document.selectSingleNode("//result_code").getStringValue())){
                        return new FebsResponse().fail(document.selectSingleNode("//err_code").getStringValue());
                    }
                    if("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//result_code").getStringValue())) {
                        //log.info("获取平台证书 {}",document.selectSingleNode("//certificates").getStringValue());
                        //成功，更新申请参数到数据库(待处理:mch_id,sign,business_code,申请成功,applyment_id)
                        params[0].put("status","申请入驻成功，待签约");
                        params[0].put("data_status","0");//生效状态
                        String applyment_id=document.selectSingleNode("//applyment_id").getStringValue();
                        params[0].put("applyment_id",applyment_id);
                        merchantApplyMapper.updateMerchantApply(params[0]);
                        return new FebsResponse().success(applyment_id);
                    }
                }else{
                    String return_msg=document.selectSingleNode("//return_msg").getStringValue();
                    params[0].put("status","申请入驻失败，"+return_msg);
                    merchantApplyMapper.updateMerchantApply(params[0]);
                    return new FebsResponse().fail(return_msg);
                }

                /*
                    if("SUCCESS".equalsIgnoreCase(document.selectSingleNode("//return_code").getStringValue())){
                        log.info("1、随机字符串 {}", document.selectSingleNode("//nonce_str").getStringValue());
                        log.info("2、签名 {}",document.selectSingleNode("//sign").getStringValue());
                        log.info("3、业务结果 {}",document.selectSingleNode("//result_code").getStringValue());
                        log.info("4、错误代码 {}",document.selectSingleNode("//err_code").getStringValue());
                        log.info("5、错误代码描述 {}",document.selectSingleNode("//err_code_des").getStringValue());
                        log.info("6、参数校验不通过的字段名 {}",document.selectSingleNode("//err_param").getStringValue());

                    }
                */
                }else{
                    params[0].put("status","申请入驻失败");
                    merchantApplyMapper.updateMerchantApply(params[0]);
                    return new FebsResponse().fail("入驻请求失败");
            }

        } catch (Exception e) {
            log.error("执行httpclient商户入驻请求错误 {}", e);
        }
        params[0].put("status","申请入驻失败,入驻请求异常");
        merchantApplyMapper.updateMerchantApply(params[0]);
        return   new FebsResponse().fail("入驻请求异常");
    }



    // 封装数据
    public Map[] merchantRegisterToMap1(MerchantRegisterVo vo){
        log.info("Vo 参数 "+vo.toString());
        Map<String,String> parampwd=new HashMap<String,String>();
        String certFicates=certFicatesService.getCertFicates();
        //解析证书，获取证书序列号
        CertficateParseUtil cu=new CertficateParseUtil();
        cu.certificateParse(certFicates);

        String original ="";
        try {
            original=certFicatesService.decryptCertSN(cu.getAssociated_data(), cu.getNonce(), cu.getCiphertext(), merchantServerConfig.getApiv3Key());
        }catch (Exception e){
            log.info(" 解析证书原文错误 "+e.getMessage());
        }

        String version ="3.0";//接口版本号
        String cert_sn=cu.getSerial_no();// 待处理 平台证书序列号

        String mch_id=merchantServerConfig.getMerchantId();//商户ID
        String nonce_str= UUID.randomUUID().toString().replace("-", "");//
        String sign_type="HMAC-SHA256";//签名类型
        String business_code=nonce_str;//vo.getBusiness_code();//业务申请编号(直接用随机码)
        String id_card_copy=vo.getId_card_copy();//身份证人像面照片
        String id_card_national=vo.getId_card_national();//身份证国徽面照片
        String id_card_valid_time=vo.getId_card_valid_time();//身份证有效期限
        String account_bank=vo.getAccount_bank();//开户银行  开户银行对照表
        String bank_address_code=vo.getBank_address_code();//开户银行省市编码

        String account_number=vo.getAccount_number();//银行账号
        String store_name=vo.getStore_name();//门店名称
        String store_address_code=vo.getStore_address_code();//门店省市编码  对照表
        String store_street=vo.getStore_street();//门店街道名称
        String store_longitude=vo.getStore_longitude();//门店经度
        String store_latitude=vo.getStore_latitude();//门店纬度
        String store_entrance_pic=vo.getStore_entrance_pic();////门店门口照片
        String indoor_pic=vo.getIndoor_pic();//店内环境照片
        String address_certification=vo.getAddress_certification();//经营场地证明
        String merchant_shortname=vo.getMerchant_shortname();//商户简称
        String service_phone=vo.getService_phone();//客服电话
        String product_desc=vo.getProduct_desc();//售卖商品/提供服务描述
        //Rate rate=rateMapper.selectByExample(vo.getRate());//

        String business_addition_desc=vo.getBusiness_addition_desc();//补充说明
        String business_addition_pics=vo.getBusiness_addition_pics();//补充材料 (最多可上传5张照片，请填写已)

        String id_card_name="";//身份证名称
        String id_card_number="";//身份证号
        String account_name="";//开户名称
        String contact="";//联系人
        String contact_phone="";//联系人电话
        String contact_email="";//联系人邮箱

        parampwd.put("version",version);
        parampwd.put("cert_sn",cert_sn);
        parampwd.put("mch_id",mch_id);
        parampwd.put("nonce_str",nonce_str);
        parampwd.put("sign_type",sign_type);
        parampwd.put("business_code",business_code);
        parampwd.put("id_card_copy",id_card_copy);
        parampwd.put("id_card_national",id_card_national);
        parampwd.put("id_card_valid_time",id_card_valid_time);
        parampwd.put("account_bank",account_bank);
        parampwd.put("bank_address_code",bank_address_code);

        parampwd.put("account_number",account_number);
        parampwd.put("store_name",store_name);
        parampwd.put("store_address_code",store_address_code);
        parampwd.put("store_street",store_street);
        if(null!=store_longitude)
            parampwd.put("store_longitude",store_longitude);
        if(null!=store_latitude)
            parampwd.put("store_latitude",store_latitude);
        parampwd.put("store_entrance_pic",store_entrance_pic);
        parampwd.put("indoor_pic",indoor_pic);
        parampwd.put("address_certification",address_certification);
        parampwd.put("merchant_shortname",merchant_shortname);
        parampwd.put("service_phone",service_phone);
        parampwd.put("product_desc",product_desc);

        if(null!=business_addition_desc)
            parampwd.put("business_addition_desc",business_addition_desc);
        parampwd.put("business_addition_pics",business_addition_pics);
        //备份一份 参数数据保存到数据库中
        Map<String,String> paramDB=new HashMap<String,String>();
        paramDB.putAll(parampwd);
        //数据库只保存对应ID即可
        paramDB.put("rate_id",vo.getRateId().toString());
//        String bank_name=vo.getBank_name();//开户银行全称（含支行）
        paramDB.put("bank_id",vo.getBankId().toString());

        String bank_name= bankService.selectByKey(vo.getBankId()).getBankName();
        if(null!=bank_name)
            parampwd.put("bank_name",bank_name);
        String rate=rateService.selectByKey(vo.getRateId()).getRate();//费率
        parampwd.put("rate",rate);


        // 加密参数，不需要保存到数据库中，但需要加密提交给微信接口
        try {
           // String encrypt = EncryptionUtils.rsaEncryptByCert("我的身份证",original);
            String id_card_name_pwd = vo.getId_card_name();//身份证姓名 (加密)
            paramDB.put("id_card_name",id_card_name_pwd);
            id_card_name =EncryptionUtils.rsaEncryptByCert(id_card_name_pwd,original);// certFicatesService.encryptPkcs1padding(id_card_name_pwd);
            String id_card_number_pwd=vo.getId_card_number();//身份证号码 (加密)
            paramDB.put("id_card_number",id_card_number_pwd);
            id_card_number =EncryptionUtils.rsaEncryptByCert(id_card_number_pwd,original) ;//certFicatesService.encryptPkcs1padding(id_card_number_pwd);
            String account_name_pwd=vo.getAccount_name();//开户名称 (加密)
            paramDB.put("account_name",account_name_pwd);
            account_name = EncryptionUtils.rsaEncryptByCert(account_name_pwd, original);//certFicatesService.encryptPkcs1padding(account_name_pwd);
            String contact_pwd=vo.getContact();//联系人姓名
            paramDB.put("contact",contact_pwd);
            contact = EncryptionUtils.rsaEncryptByCert(contact_pwd, original);//certFicatesService.encryptPkcs1padding(contact_pwd);
            String contact_phone_pwd = vo.getContact_phone();//手机号码 (加密)
            paramDB.put("contact_phone",contact_phone_pwd);
            contact_phone=EncryptionUtils.rsaEncryptByCert(contact_phone_pwd, original);//certFicatesService.encryptPkcs1padding(contact_phone_pwd);
            String contact_email_pwd = vo.getContact_email();//联系邮箱 (加密)
            paramDB.put("contact_email",contact_email_pwd);
            if(null!=contact_email_pwd) {
                contact_email = EncryptionUtils.rsaEncryptByCert(contact_email_pwd, original);//certFicatesService.encryptPkcs1padding(contact_email_pwd);
            }

        }catch (Exception e){
            log.info("加密失败 {}",e.getMessage());
        }

        parampwd.put("account_name",account_name);////开户名称 (加密)
        parampwd.put("id_card_number",id_card_number);//身份证号码 (加密)
        parampwd.put("id_card_name",id_card_name);////身份证姓名 (加密)
        parampwd.put("contact",contact);////联系人姓名
        parampwd.put("contact_phone",contact_phone);//手机号码 (加密)
        parampwd.put("contact_email",contact_email);//联系邮箱 (加密)
        log.info("参数:"+paramDB.toString());


        return new Map[]{paramDB,parampwd};
    }

    public Map[] merchantRegisterToMap(MerchantRegisterVo vo){
        log.info("Vo 参数 "+vo.toString());
        Map<String,String> parampwd=new HashMap<String,String>();
        parampwd=object2Map(vo);

        String certFicates=certFicatesService.getCertFicates();
        //解析证书，获取证书序列号
        CertficateParseUtil cu=new CertficateParseUtil();
        cu.certificateParse(certFicates);

        String original ="";
        try {
            original=certFicatesService.decryptCertSN(cu.getAssociated_data(), cu.getNonce(), cu.getCiphertext(), merchantServerConfig.getApiv3Key());
        }catch (Exception e){
            log.info(" 解析证书原文错误 "+e.getMessage());

        }
        String version ="3.0";//接口版本号
        String cert_sn=cu.getSerial_no();// 待处理 平台证书序列号
        String mch_id=merchantServerConfig.getMerchantId();//商户ID
        String nonce_str= UUID.randomUUID().toString().replace("-", "");//
        String sign_type="HMAC-SHA256";//签名类型
        String business_code=nonce_str;//vo.getBusiness_code();//业务申请编号(直接用随机码)

        parampwd.put("version",version);
        parampwd.put("cert_sn",cert_sn);
        parampwd.put("mch_id",mch_id);
        parampwd.put("nonce_str",nonce_str);
        parampwd.put("sign_type",sign_type);
        parampwd.put("business_code",business_code);

        //获取对应的数据ID->name
        //bankId->account_bank ,bank_name	否	(支行)
        if(parampwd.containsKey("bankId")){
            String account_bank= bankService.selectByKey(vo.getBankId()).getBankName();
            parampwd.put("account_bank",account_bank);
        }
        if(parampwd.containsKey("rateId")){
            String rate_pwd=rateService.selectByKey(vo.getRateId()).getRate();//费率
            parampwd.put("rate",rate_pwd);
        }

        //备份一份 参数数据保存到数据库中
        Map<String,String> paramDB=new HashMap<String,String>();
        paramDB.putAll(parampwd);
        // 再处理需要加密的参数
        String id_card_name="";//身份证名称
        String id_card_number="";//身份证号
        String account_name="";//开户名称
        String contact="";//联系人
        String contact_phone="";//联系人电话
        String contact_email="";//联系人邮箱
        try{
            if(parampwd.containsKey("id_card_name")){
                parampwd.put("id_card_name", EncryptionUtils.rsaEncryptByCert(parampwd.get("id_card_name"),original));
            }
            if(parampwd.containsKey("id_card_number")){
                parampwd.put("id_card_number", EncryptionUtils.rsaEncryptByCert(parampwd.get("id_card_number"),original));
            }
            if(parampwd.containsKey("account_name")){
                parampwd.put("account_name", EncryptionUtils.rsaEncryptByCert(parampwd.get("account_name"),original));
            }
            if(parampwd.containsKey("contact")){
                parampwd.put("contact", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact"),original));
            }
            if(parampwd.containsKey("contact_phone")){
                parampwd.put("contact_phone", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact_phone"),original));
            }
            if(parampwd.containsKey("contact_email")){
                parampwd.put("contact_email", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact_email"),original));
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return new Map[]{paramDB,parampwd};
    }

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return
     */
    public  Map<String, String> object2Map(Object obj) {
        Map<String, String> map = new HashMap<String, String>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if(null==field.get(obj)) continue;//为空则剔除
                map.put(field.getName(), field.get(obj).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 参数检查函数
     * @param paramMap
     */
    public void checkParam(Map<String,String> paramMap){
        log.info(paramMap.toString());
        String[] needParamList=new String[]{
                "version","cert_sn","mch_id","nonce_str","sign_type","sign","business_code","id_card_copy","id_card_national","id_card_name","id_card_number","id_card_valid_time","account_name",
                "bank_address_code","account_number","store_name","store_address_code","store_street","store_entrance_pic",
                "indoor_pic","merchant_shortname","service_phone","product_desc","contact","contact_phone","rate","account_bank"
        };
        String[] noNeedParamList=new String[]{"address_certification","contact_email","bank_name","store_longitude","store_latitude","business_addition_desc","business_addition_pics"};
        int countNeed=0;
        for(String s:needParamList){
            if(!paramMap.containsKey(s)) {

                log.info("未提供必要参数:"+s);
            }else{
                countNeed++;
            }
        }
        log.info("-------------必要参数总数："+needParamList.length+"个,已提供了"+countNeed+"个" );
        countNeed=0;
        for(String s:noNeedParamList){
            if(!paramMap.containsKey(s)) {
                log.info("未提供非必要参数"+s);
            }else{
                countNeed++;
            }
        }
        log.info("-------------非必要参数总数："+noNeedParamList.length+"个,已提供了"+countNeed+"个" );



    }
}
