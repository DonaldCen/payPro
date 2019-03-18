package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.wx.OrderInfoMapper;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.service.OrderInfoService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Service
public class OrderInfoServiceImpl extends BaseService<OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Override
    public int createOrderInfoByUserId(OrderInfo info) {
        return orderInfoMapper.createOrderInfoByUserId(info);
    }
}
