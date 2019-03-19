package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.ProductInfo;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
public interface OrderInfoService extends IService<OrderInfo> {
    int createOrderInfoByUserId(OrderInfo info);

    /**
     *  1.创建订单
     *  2.调用微信接口
     *  3.生成微信支付码
     */
    void createOrderInfo(ProductInfo info);
}
