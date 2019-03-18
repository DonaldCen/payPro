package yx.pay.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.controller.BaseController;
import yx.pay.system.domain.wx.ProductInfo;
import yx.pay.system.service.ProductService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/18
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/product")
public class ProductInfoController extends BaseController {
    @Autowired
    private ProductService productService;

    @PostConstruct
    private void init(){
        ProductInfo info = new ProductInfo();
        info.setName("test");
        info.setTotalFee(100d);
        info.setDescription("产品描述");
        productService.save(info);
    }

}
