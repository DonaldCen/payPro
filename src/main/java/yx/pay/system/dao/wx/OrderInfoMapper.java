package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.OrderInfo;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Component
public interface OrderInfoMapper extends MyMapper<OrderInfo> {
    int createOrderInfoByUserId(OrderInfo info);
}
