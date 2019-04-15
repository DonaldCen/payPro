package yx.pay.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;

import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.impl.BaseService;
import yx.pay.common.utils.PollUtil;
import yx.pay.common.utils.WxHttpUtil;
import yx.pay.common.utils.WxUtil;
import yx.pay.system.dao.wx.OrderInfoMapper;
import yx.pay.system.domain.wx.*;
import yx.pay.system.service.ChannelMerchantService;
import yx.pay.system.service.MerchantApplyService;
import yx.pay.system.service.OrderInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends BaseService<OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private WxUtil wxUtil;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    public MerchantServerConfig merchantServerConfig;
    @Autowired
    private MerchantApplyService merchantApplyService;
    @Autowired
    private ChannelMerchantService channelMerchantService;

    @Override
    public int createOrderInfoByUserId(OrderInfo info) {
        return orderInfoMapper.createOrderInfoByUserId(info);
    }

    /**
     * 1.创建订单
     * 2.调用微信接口
     * 3.生成微信支付码
     */
    @Override
    public FebsResponse createOrderInfo(OrderInfoVo orderInfoVo) throws Exception {
        String orderNo = wxUtil.createOrderNoByPayType(PayTypeEnum.WECHAT_PAY);
        saveOrderInfo(orderInfoVo, orderNo);
        return wxPay(orderInfoVo, orderNo,null,WxPayTypeEnum.NATIVE.getType());
    }

    @Override
    public FebsResponse jsApiPay(OrderInfoVo orderInfoVo, String openId) throws Exception {
        String orderNo = wxUtil.createOrderNoByPayType(PayTypeEnum.WECHAT_PAY);
        saveOrderInfo(orderInfoVo, orderNo);
        return wxPay(orderInfoVo, orderNo,openId,WxPayTypeEnum.JSAPI.getType());
    }

    private Integer getUserId(String chlMerchanNo) {
        return channelMerchantService.getIdByMerchantNo(chlMerchanNo);
    }
    private FebsResponse wxPay(OrderInfoVo orderInfoVo, String orderNo,String openId,int payType) throws Exception {
        FebsResponse response = new FebsResponse();
        SortedMap<String, String> param = null;
        String non = UUID.randomUUID().toString().replace("-", "");
        if(payType == WxPayTypeEnum.NATIVE.getType()){
            param = wechatPayNativeModel(orderInfoVo, orderNo,non);
        }else if(payType == WxPayTypeEnum.JSAPI.getType()){
            param = wechatPayJsApiModel(orderInfoVo,orderNo,openId,non);
        }
        String requestXml = WXPayUtil.generateSignedXml(param, merchantServerConfig.getApiKey());
        String resXml = WxHttpUtil.postData(WxUtil.UNIFIED_ORDER_URL, requestXml);
        Map<String, String> responseMap = WXPayUtil.xmlToMap(resXml);
        //判断交易状态
        if ("FAIL".equalsIgnoreCase(responseMap.get("return_code"))) {
            response.fail(responseMap.get("return_msg"));
            return response;
        }
        //判断交易状态
        if ("FAIL".equalsIgnoreCase(responseMap.get("result_code"))) {
            response.fail(responseMap.get("err_code_des"));
            return response;
        }
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resXml);
        resultMap.put("trade_type", responseMap.get("trade_type"));
        resultMap.put("prepay_id", responseMap.get("prepay_id"));
        resultMap.put("code_url", responseMap.get("code_url"));
        String result = sendWeixinPayPageParam(orderNo,non,responseMap.get("prepay_id"));
        log.info("result=[{}]", JSON.toJSON(resultMap));
        response.success(result);
        return response;
    }

    //传递回微信支付页面的必需参数 appId timeStamp nonceStr package signType
    public String sendWeixinPayPageParam(String out_trade_no, String noncestr,String prepay_id) {
        String resultString;
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        finalpackage.put("appId", wxConfig.getAppId());
        finalpackage.put("timeStamp", getTimeStamp());
        finalpackage.put("nonceStr", noncestr);
        finalpackage.put("package", "prepay_id="+prepay_id);
        finalpackage.put("signType", "MD5");
        String finalsign = PaymentKit.createSign(finalpackage, merchantServerConfig.getApiKey());
        finalpackage.put("sign", finalsign);
        finalpackage.put("out_trade_no", out_trade_no);
        resultString = JsonUtils.toJson(finalpackage);
        return resultString;
    }

    public String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    private void saveOrderInfo(OrderInfoVo orderInfoVo, String orderNo) throws Exception {
        double totalFee = orderInfoVo.getTotal_fee() * 100;
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayType(PayTypeEnum.WECHAT_PAY.getType());
        orderInfo.setStatus(OrderStatusEnum.PREPAYMENT.getIndex());
        orderInfo.setOrderNo(orderNo);
        orderInfo.setTotalFee(totalFee);
        String merchantNo = orderInfoVo.getChlMerchanNo();
        orderInfo.setMchNo(merchantNo);
        Integer userId = getUserId(merchantNo);
        if (userId == null) {
            throw new Exception("please check chlMerchanNo !");
        }
        orderInfo.setUserId(userId);
        orderInfoMapper.insert(orderInfo);
    }

    private SortedMap<String, String> wechatPayNativeModel(OrderInfoVo orderInfoVo, String orderNo,String nonceStr) {
        SortedMap<String, String> param = new TreeMap<>();
        //trade_type
        param.put("trade_type", "NATIVE");
        payOrder(orderInfoVo,orderNo,nonceStr,param);
        return param;
    }
    private SortedMap<String, String> wechatPayJsApiModel(OrderInfoVo orderInfoVo, String orderNo,String openId,String non) {
        SortedMap<String, String> param = new TreeMap<>();
        //trade_type
        param.put("trade_type", "JSAPI");
        // device_info
        param.put("device_info", "WEB");
        param.put("openid", openId);
        payOrder(orderInfoVo,orderNo,non,param);
        return param;
    }

    private String getOpenId(String code) {
        return null;
    }

    private SortedMap<String, String> payOrder(OrderInfoVo orderInfoVo, String orderNo,String nonceStr,SortedMap<String, String> param) {
        /**
         * 公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID
         * 商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
         * 子商户号	sub_mch_id	是	String(32)	1900000109	微信支付分配的子商户号
         * 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
         * 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
         * 商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
         * 商品描述交易字段格式根据不同的应用场景建议按照以下格式上传：
         *
         * （1）PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
         *
         * （2） 公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
         *
         * （3） H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
         *
         * （4） 线下门店——门店品牌名-城市分店名-实际商品名称，例如： image形象店-深圳腾大- QQ公仔）
         *
         * （5） APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
         *
         * 商户订单号	out_trade_no	是	String(32)	1217752501201407033233368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
         * 总金额	total_fee	是	Int	888	订单总金额，只能为整数，详见支付金额
         * 终端IP	spbill_create_ip	是	String(16)	123.12.12.123	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
         * 通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
         * 交易类型	trade_type	是	String(16)	JSAPI
         * JSAPI -JSAPI支付
         * NATIVE -Native支付
         * APP -APP支付
         */
        //appid
        param.put("appid", wxConfig.getAppId());
        //mch_id
        param.put("mch_id", merchantServerConfig.getMerchantId());
        String sub_mch_id = getSubMchId();
        //sub_mch_id
        param.put("sub_mch_id", sub_mch_id);
        //nonce_str
        param.put("nonce_str", nonceStr);
        //body
        param.put("body", orderInfoVo.getBody());
        //out_trade_no
        param.put("out_trade_no", orderNo);
        //total_fee
        param.put("total_fee", String.valueOf(orderInfoVo.getTotal_fee()));
        //spbill_create_ip
        param.put("spbill_create_ip", "116.22.197.15");
        //notify_url
        param.put("notify_url", wxConfig.getNotifyUrl());
        return param;
    }


    /**
     * 获取 sub_mch_id 方法
     */
    private String getSubMchId() {
        List<String> subMchIdList = merchantApplyService.getSubMchIdList(SignStatusEnum.FINISH.getValue());
        PollUtil.setAll(subMchIdList);
        return PollUtil.next();
    }

    @Override
    public List<OrderInfo> findOrderList(QueryRequest request, OrderInfo orderInfo) {
        try {
            if (request.getSortField() != null) {
                orderInfo.setSortField(request.getSortField());
                if (StringUtils.equals(FebsConstant.ORDER_DESC, request.getSortOrder()))
                    orderInfo.setSortOrder("desc");
                else
                    orderInfo.setSortOrder("asc");
            }
            return this.orderInfoMapper.findOrderList(orderInfo);
        } catch (Exception e) {
            log.error("查询流水异常", e);
            return new ArrayList<>();
        }
    }


}
