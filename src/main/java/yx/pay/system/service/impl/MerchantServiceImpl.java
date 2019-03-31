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
import yx.pay.common.utils.*;
import yx.pay.system.dao.BankMapper;
import yx.pay.system.dao.RateMapper;
import yx.pay.system.dao.wx.MerchantApplyMapper;
import yx.pay.system.dao.wx.MerchantMapper;
import yx.pay.system.domain.Rate;
import yx.pay.system.domain.vo.MerchantRegisterVo;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.domain.wx.SignStatusEnum;
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
    private WxHttpsUtil wxHttpsUtil;

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
     * 商户入驻申请
     * 只接收HMAC-SHA256  签名
     *
     * @param vo
     * @return applyment_id
     */
    public FebsResponse merchantApply(MerchantRegisterVo vo) {
        FebsResponse response = new FebsResponse();
        String applyUrl = "https://api.mch.weixin.qq.com/applyment/micro/submit";
        // 获取数据库参数和加密参数Map
        Map<String, String>[] params = merchantRegisterToMap(vo);
        try {
            String signStr = SignUtil.wechatCertficatesSignBySHA256(params[1], merchantServerConfig.getApiKey());
            params[0].put("sign", signStr);
            params[1].put("sign", signStr);
            log.info("商户入驻申请接口签名 " + signStr);
            String requestDataXml = WXPayUtil.generateSignedXml(params[1], merchantServerConfig.getApiKey(), WXPayConstants.SignType.HMACSHA256);
            log.info("xml 格式串 {}", requestDataXml);
            //boolean flag=WXPayUtil.isSignatureValid(requestDataXml,merchantServerConfig.getApiKey());
            // log.info("签名验证标识"+flag);
            checkParam(params[1]);
            // 先保存申请数据到数据库中(状态 待签中)
            params[0].put("status", SignStatusEnum.TO_BE_SIGNED.getValue());
            //获取新的主键ID
            Long id = merchantApplyMapper.getMerchantApplyNextID();
            params[0].put("id", id.toString());
//            params[1].put("id",id.toString());
            params[0].put("data_status", "1");//初始失效状态，成功则会修改为生效状态
            // 申请参数入库
            merchantApplyMapper.addMerchantApply(params[0]);
            //调用方法请求(商户入驻申请请求)
            String responseDataXml = wxHttpsUtil.call(applyUrl, requestDataXml, merchantServerConfig);
            Map<String, String> responseMap = WXPayUtil.xmlToMap(responseDataXml);
            //判断申请入驻状态
            if ("FAIL".equalsIgnoreCase(responseMap.get("return_code"))) {
                params[0].put("apply_desc", responseMap.get("return_msg"));
                response.fail(responseMap.get("return_msg"));
                merchantApplyMapper.updateMerchantApply(params[0]);
                return response;
            }
            //判断申请入驻状态
            if ("FAIL".equalsIgnoreCase(responseMap.get("result_code"))) {
                params[0].put("apply_desc", responseMap.get("err_code_des"));
                response.fail(responseMap.get("err_code_des"));
                merchantApplyMapper.updateMerchantApply(params[0]);
                return response;
            }
            //申请入驻成功！
            if (responseMap.containsKey("applyment_id")) {
                params[0].put("data_status", "0");//生效状态
                params[0].put("applyment_id", responseMap.get("applyment_id"));
                merchantApplyMapper.updateMerchantApply(params[0]);
                return response.success(responseMap.get("applyment_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("商户入驻请求失败 {}", e.getMessage());
        }
        return response.fail("商户入驻请求失败!");
    }

    /**
     * 参数生成Map数组，一份数据数据，另外一份包含加密的过的，提交给微信接口数据
     *
     * @param vo
     * @return
     */
    public Map[] merchantRegisterToMap(MerchantRegisterVo vo) {
        //log.info("Vo 参数 "+vo.toString());
        Map<String, String> parampwd = new HashMap<String, String>();
        parampwd = object2Map(vo);
        String certFicates = certFicatesService.getCertFicates();
        //解析证书，获取证书序列号
        CertficateParseUtil cu = new CertficateParseUtil();
        cu.certificateParse(certFicates);
        String original = "";
        try {
            original = certFicatesService.decryptCertSN(cu.getAssociated_data(), cu.getNonce(), cu.getCiphertext(), merchantServerConfig.getApiv3Key());
        } catch (Exception e) {
            log.info(" 解析证书原文错误 " + e.getMessage());
        }
        String version = "3.0";//接口版本号
        String cert_sn = cu.getSerial_no();// 待处理 平台证书序列号
        String mch_id = merchantServerConfig.getMerchantId();//商户ID
        String nonce_str = UUID.randomUUID().toString().replace("-", "");//
        String sign_type = "HMAC-SHA256";//签名类型
        String business_code = nonce_str;//vo.getBusiness_code();//业务申请编号(直接用随机码)

        parampwd.put("version", version);
        parampwd.put("cert_sn", cert_sn);
        parampwd.put("mch_id", mch_id);
        parampwd.put("nonce_str", nonce_str);
        parampwd.put("sign_type", sign_type);
        parampwd.put("business_code", business_code);

        //获取对应的数据ID->name
        //bankId->account_bank ,bank_name	否	(支行)
        if (parampwd.containsKey("bankId")) {
            String account_bank = bankService.selectByKey(vo.getBankId()).getBankName();
            parampwd.put("account_bank", account_bank);
        }
        if (parampwd.containsKey("rateId")) {
            String rate_pwd = rateService.selectByKey(vo.getRateId()).getRate();//费率
            parampwd.put("rate", rate_pwd);
        }

        //备份一份 参数数据保存到数据库中
        Map<String, String> paramDB = new HashMap<String, String>();
        paramDB.putAll(parampwd);
        // 再处理需要加密的参数
        String id_card_name = "";//身份证名称
        String id_card_number = "";//身份证号
        String account_name = "";//开户名称
        String contact = "";//联系人
        String contact_phone = "";//联系人电话
        String contact_email = "";//联系人邮箱
        try {
            if (parampwd.containsKey("id_card_name")) {
                parampwd.put("id_card_name", EncryptionUtils.rsaEncryptByCert(parampwd.get("id_card_name"), original));
            }
            if (parampwd.containsKey("id_card_number")) {
                parampwd.put("id_card_number", EncryptionUtils.rsaEncryptByCert(parampwd.get("id_card_number"), original));
            }
            if (parampwd.containsKey("account_name")) {
                parampwd.put("account_name", EncryptionUtils.rsaEncryptByCert(parampwd.get("account_name"), original));
            }
            if (parampwd.containsKey("contact")) {
                parampwd.put("contact", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact"), original));
            }
            if (parampwd.containsKey("contact_phone")) {
                parampwd.put("contact_phone", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact_phone"), original));
            }
            if (parampwd.containsKey("contact_email")) {
                parampwd.put("contact_email", EncryptionUtils.rsaEncryptByCert(parampwd.get("contact_email"), original));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Map[]{paramDB, parampwd};
    }

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return
     */
    public Map<String, String> object2Map(Object obj) {
        Map<String, String> map = new HashMap<String, String>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (null == field.get(obj)) continue;//为空则剔除
                map.put(field.getName(), field.get(obj).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 参数检查函数
     *
     * @param paramMap
     */
    public void checkParam(Map<String, String> paramMap) {
        log.info(paramMap.toString());
        String[] needParamList = new String[]{
                "version", "cert_sn", "mch_id", "nonce_str", "sign_type", "sign", "business_code", "id_card_copy", "id_card_national", "id_card_name", "id_card_number", "id_card_valid_time", "account_name",
                "bank_address_code", "account_number", "store_name", "store_address_code", "store_street", "store_entrance_pic",
                "indoor_pic", "merchant_shortname", "service_phone", "product_desc", "contact", "contact_phone", "rate", "account_bank"
        };
        String[] noNeedParamList = new String[]{"address_certification", "contact_email", "bank_name", "store_longitude", "store_latitude", "business_addition_desc", "business_addition_pics"};
        int countNeed = 0;
        for (String s : needParamList) {
            if (!paramMap.containsKey(s)) {

                log.info("未提供必要参数:" + s);
            } else {
                countNeed++;
            }
        }
        log.info("-------------必要参数总数：" + needParamList.length + "个,已提供了" + countNeed + "个");
        countNeed = 0;
        for (String s : noNeedParamList) {
            if (!paramMap.containsKey(s)) {
                log.info("未提供非必要参数" + s);
            } else {
                countNeed++;
            }
        }
        log.info("-------------非必要参数总数：" + noNeedParamList.length + "个,已提供了" + countNeed + "个");


    }
}
