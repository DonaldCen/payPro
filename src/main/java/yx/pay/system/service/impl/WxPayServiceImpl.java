package yx.pay.system.service.impl;

import org.springframework.stereotype.Service;

import yx.pay.system.service.WxPayService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Service
public class WxPayServiceImpl implements WxPayService {
    /**
     * 生成二维码图片
     */
    @Override
    public String generateQrCodeImages() {
        return null;
    }

    /**
     * 扫码支付 模式1
     */
    @Override
    public String sweepQrCodeToPayModeOne() {
        return null;
    }
}
