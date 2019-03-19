package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yx.pay.common.service.impl.BaseService;
import yx.pay.common.utils.WxUtil;
import yx.pay.system.dao.wx.OrderInfoMapper;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.OrderStatusEnum;
import yx.pay.system.domain.wx.PayTypeEnum;
import yx.pay.system.domain.wx.ProductInfo;
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
    @Autowired
    private WxUtil wxUtil;
    @Override
    public int createOrderInfoByUserId(OrderInfo info) {
        return orderInfoMapper.createOrderInfoByUserId(info);
    }
    /**
     *  1.创建订单
     *  2.调用微信接口
     *  3.生成微信支付码
     */
    @Override
    public void createOrderInfo(ProductInfo info) {
        String orderNo = wxUtil.createOrderNoByPayType(PayTypeEnum.WECHAT_PAY);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayType(PayTypeEnum.WECHAT_PAY.getType());
        orderInfo.setStatus(OrderStatusEnum.PREPAYMENT.getIndex());
        orderInfo.setOrderNo(orderNo);
        orderInfo.setProductId(info.getId());
        //todo
    }


}
