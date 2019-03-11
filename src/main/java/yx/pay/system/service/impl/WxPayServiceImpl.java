package yx.pay.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.SortedMap;
import java.util.TreeMap;

import yx.pay.common.utils.QrCodeUtil;
import yx.pay.common.utils.WxUtil;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.WxConfig;
import yx.pay.system.service.WxPayService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WxUtil util;
    @Autowired
    private QrCodeUtil qrCodeUtil;


    /**
     * 生成二维码图片
     */
    @Override
    public String generateQrCodeImages(OrderInfo orderInfo) {
        SortedMap<Object, Object> packageParams = new TreeMap<>();
        //封装通用参数
        util.commonParams(packageParams);

        packageParams.put("product_id", orderInfo.getOrderNo());//真实商品ID
        packageParams.put("time_stamp", util.getCurrTime());

        //生成签名
        String sign = util.createSign("UTF-8", packageParams);
        //组装二维码信息
        String qrInfo = util.buildQrCodeInfo(packageParams,sign);
        log.info("qrCode info:[{}]",qrInfo);
//        qrCodeUtil.createZxingqrCode(qrInfo,);
        return qrInfo;
    }

    /**
     * 扫码支付 模式1
     */
    @Override
    public String sweepQrCodeToPayModeOne() {
        return null;
    }

    /**
     * 长链改短链
     */
    @Override
    public String getShortUrl(String longUrl) {
        return null;
    }
}
