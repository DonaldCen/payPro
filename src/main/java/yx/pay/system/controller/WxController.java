package yx.pay.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsResponse;
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
@Validated
@RestController
@RequestMapping("wx")
public class WxController {
    @Autowired
    private WxPayService wxPayService;

    @PostMapping("generateQrCode")
    public FebsResponse generateQrCode(OrderInfo info){
        try{
            wxPayService.generateQrCodeImages(info);
        }catch (Exception e){
            log.error("generateQrCode error..",e);
        }
        return null;
    }


}
