package yx.pay.system.service;

import yx.pay.system.domain.wx.OrderInfo;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
public interface OrderInfoService {
    int createOrderInfoByUserId(OrderInfo info);
}
