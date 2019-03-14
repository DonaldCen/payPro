package yx.pay.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import yx.pay.common.utils.QrCodeUtil;
import yx.pay.common.utils.WxHttpUtil;
import yx.pay.common.utils.WxUtil;
import yx.pay.common.utils.XMLUtil;
import yx.pay.system.domain.wx.*;
import yx.pay.system.service.MerchantService;
import yx.pay.system.service.OrderInfoService;
import yx.pay.system.service.WxPayService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WxUtil util;
    @Autowired
    private QrCodeUtil qrCodeUtil;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private MerchantService merchantService;


    /**
     * 生成二维码图片
     */
    @Override
    @Transactional
    public void generateQrCodeImages(int userId) {
        OrderInfo info = new OrderInfo();
        info.setPayType(OrderPayTypeEnum.CREATE_QR_CODE.getIndex());
        info.setStatus(OrderStatusEnum.CREATE_QR_CODE.getIndex());
        info.setUserId(userId);
        //创建验证码状态的订单
        int orderId = orderInfoService.createOrderInfoByUserId(info);
        String qrInfo = buildQrInfo(orderId);
        String url = qrCodeUtil.getQrCodePicName( String.valueOf(userId));
        //生成付款二维码图片
        qrCodeUtil.createQrCode(util.getShortUrl(qrInfo),url);
        Merchant merchant = new Merchant();
        merchant.setUserId(userId);
        merchant.setQrCoreUrl(url);
        //保存商户付款二维码图片
        merchantService.updateMerchantQrUrl(merchant);
    }

    /**
     * 扫码支付 模式1
     */
    @Override
    public String sweepQrCodeToPayModeOne() {
        return null;
    }



    private String buildQrInfo(int orderId){
        SortedMap<Object, Object> packageParams = new TreeMap<>();
        //封装通用参数
        util.commonParams(packageParams);

        packageParams.put("product_id", orderId);//真实商品ID
        packageParams.put("time_stamp", util.getCurrTime());

        //生成签名
        String sign = util.createSign("UTF-8", packageParams);
        //组装二维码信息
        String qrInfo = util.buildQrCodeInfo(packageParams,sign);
        log.info("qrCodeInfo:[{}]",qrInfo);

        return qrInfo;
    }
}
