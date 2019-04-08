package yx.pay.system.service.impl;

import com.github.wxpay.sdk.WXPayUtil;

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

import javax.annotation.PostConstruct;

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
    public void generateQrCodeImages(int merchantId, int productId) throws Exception {
        String qrInfo = buildQrInfo(productId);
        String url = qrCodeUtil.getQrCodePicName(String.valueOf(merchantId));
        //生成付款二维码图片
        qrCodeUtil.createQrCode(util.getShortUrl(qrInfo), url);
        Merchant merchant = merchantService.selectByKey(merchantId);
        if (merchant != null) {
            merchant.setQrCoreUrl(url);
            //保存商户付款二维码图片
            merchantService.updateMerchantQrUrl(merchant);
        }

    }


    private String buildQrInfo(int orderId) throws Exception {
        SortedMap<String, String> packageParams = new TreeMap<>();
        //封装通用参数
        util.commonParams(packageParams);

        packageParams.put("product_id", String.valueOf(orderId));//真实商品ID
        packageParams.put("time_stamp", util.getCurrTime());

        //生成签名
//        String sign = util.createSign("UTF-8", packageParams);
        String sign = util.createSign(packageParams);
        //组装二维码信息
        String qrInfo = util.buildQrCodeInfo(packageParams, sign);

        log.info("qrCodeInfo:[{}]", qrInfo);

        return qrInfo;
    }
}
