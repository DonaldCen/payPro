package yx.pay.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.utils.QrCodeUtil;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.WxConfig;
import yx.pay.system.service.OrderInfoService;
import yx.pay.system.service.WxPayService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/10
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wx")
public class WxController {
    @Autowired
    private WxPayService wxPayService;
    /**
     * 指定地方，生成url,然后存储，然后展示
     *
     */
    @PostMapping("generateQrCode")
    public FebsResponse generateQrCode(int userId) {
        try {
            wxPayService.generateQrCodeImages(userId);
        } catch (Exception e) {
            log.error("generateQrCode error..", e);
            return new FebsResponse().fail(e.getMessage());
        }
        return new FebsResponse().success();
    }


}
