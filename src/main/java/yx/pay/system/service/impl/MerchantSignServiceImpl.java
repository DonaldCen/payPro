package yx.pay.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.service.impl.BaseService;
import yx.pay.common.utils.WxHttpsUtil;
import yx.pay.system.dao.wx.MerchantApplyMapper;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.domain.wx.MerchantServerConfig;
import yx.pay.system.domain.wx.SignStatusEnum;
import yx.pay.system.service.MerchantSignService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class MerchantSignServiceImpl extends BaseService<MerchantApply> implements MerchantSignService {

    private static final String signUrl = "https://api.mch.weixin.qq.com/applyment/micro/getstate";

    @Autowired
    public MerchantServerConfig merchantServerConfig;

    @Autowired
    private WxHttpsUtil wxHttpsUtil;

    @Autowired
    private MerchantApplyMapper merchantApplyMapper;

    @Override
    public FebsResponse merchantSignApply(String applymentId) {
        FebsResponse response = null;
        try {
            //查询申请状态
            response = this.merchantSignQuery(applymentId);
            return response;
        } catch (Exception e) {
            log.error("", e);
            response = new FebsResponse();
            response.fail(e.getMessage());
            return response;
        }
    }

    @Override
    public FebsResponse merchantSignUpStatus(String applymentId) {
        FebsResponse response = null;
        try {
            //查询申请状态
            response = this.merchantSignQuery(applymentId);

            Map<String, String> map = (Map<String, String>)response.get("data");
            //如果data为空，不更新返回
            if(map==null || map.isEmpty()) {
                return response;
            }
            //如果applymentState为空，不更新返回
            String state = map.get("applymentState");
            if(StringUtils.isEmpty(state)) {
                return response;
            }
            int upNum = 0;
            //为TO_BE_SIGNED:待签约、FINISH:完成两种状态
            //根据applyment_id更新小微商户号和状态到t_merchant_apply表
            if(SignStatusEnum.TO_BE_SIGNED.getValue().equalsIgnoreCase(state) || SignStatusEnum.FINISH.getValue().equalsIgnoreCase(state)) {
                log.info("开始更新小微商户号和状态");
                MerchantApply merchantApply = new MerchantApply();
                merchantApply.setApplymentID(applymentId);
                merchantApply.setSubMchId(map.get("subMchId"));
                merchantApply.setSignUrl(map.get("signUrl"));
                merchantApply.setStatus(state);
                upNum = updateSubMchIdAndStatus(merchantApply);
            }
            //否则根据applyment_id更新状态到t_merchant_apply表
            else {
                log.info("开始更新状态");
                MerchantApply merchantApply = new MerchantApply();
                merchantApply.setApplymentID(applymentId);
                merchantApply.setStatus(state);
                if(map.containsKey("auditDetail")){
                    merchantApply.setApply_desc(map.get("auditDetail"));
                }
                upNum = updateSubMchIdAndStatus(merchantApply);
            }
            if(upNum > 0) {
                log.info("更新状态成功");
                response = new FebsResponse();
                response.success();
            }
            else {
                response = new FebsResponse();
                response.fail("更新状态失败");
            }
            return response;
        } catch (Exception e) {
            log.error("", e);
            response = new FebsResponse();
            response.fail(e.getMessage());
            return response;
        }
    }

    private int updateSubMchIdAndStatus(MerchantApply merchantApply) {
        return merchantApplyMapper.updateSubMchIdAndStatus(merchantApply);
    }


    private int updateStatus(String applymentId, String status) {
        MerchantApply merchantApply = new MerchantApply();
        merchantApply.setApplymentID(applymentId);
        merchantApply.setStatus(status);
        return merchantApplyMapper.updateStatus(merchantApply);
    }

    private FebsResponse merchantSignQuery(String applymentId) throws Exception {
        Map<String, String> param = new HashMap<>();
        //版本号
        param.put("version", "1.0");
        //商户号
        param.put("mch_id", merchantServerConfig.getMerchantId());
        //随机串
        param.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        //加签算法
        param.put("sign_type", "HMAC-SHA256");
        //商户申请单号
        param.put("applyment_id", applymentId);

        FebsResponse response = new FebsResponse();
        try {
            String requestXml = WXPayUtil.generateSignedXml(param, merchantServerConfig.getApiKey(), WXPayConstants.SignType.HMACSHA256);
            String responseXml = wxHttpsUtil.call(signUrl, requestXml, merchantServerConfig);
            Map<String, String> responseMap = WXPayUtil.xmlToMap(responseXml);

            //判断交易状态
            if("FAIL".equalsIgnoreCase(responseMap.get("return_code"))) {
                response.fail(responseMap.get("return_msg"));
                return response;
            }
            //判断交易状态
            if("FAIL".equalsIgnoreCase(responseMap.get("result_code"))) {
                response.fail(responseMap.get("err_code_des"));
                return response;
            }

            Map<String, String> resultMap = WXPayUtil.xmlToMap(responseXml);
            resultMap.put("applymentId", responseMap.get("applyment_id"));
            resultMap.put("applymentState", responseMap.get("applyment_state"));
            resultMap.put("applymentStateDesc", responseMap.get("applyment_state_desc"));

            //判断业务状态
            // 为TO_BE_SIGNED:待签约、FINISH:完成两种状态，返回小微商户号和签约链接
            String state = responseMap.get("applyment_state");
            if(SignStatusEnum.TO_BE_SIGNED.getValue().equalsIgnoreCase(state) || SignStatusEnum.FINISH.getValue().equalsIgnoreCase(state)) {
                resultMap.put("subMchId", responseMap.get("sub_mch_id"));
                resultMap.put("signUrl", responseMap.get("sign_url"));
            }
            //为REJECTED:已驳回，返回审核详情
            else if(SignStatusEnum.REJECTED.getValue().equalsIgnoreCase(state)) {
                JSONObject jsonObject = JSONObject.parseObject(responseMap.get("audit_detail"));
                JSONArray jsonArray = jsonObject.getJSONArray("audit_detail");
                StringBuilder sb = new StringBuilder();
                for(Iterator iter = jsonArray.iterator(); iter.hasNext();) {
                    JSONObject auditObject = (JSONObject)iter.next();
                    sb.append(auditObject.getString("param_name"))
                            .append(":")
                            .append(auditObject.getString("reject_reason"))
                            .append(";");
                }
                resultMap.put("auditDetail", sb.toString());
            }

            response.success(resultMap);
        } catch (Exception e) {
            log.error("", e);
            response.fail(e.getMessage());
            return response;
        }
        return response;
    }
}
