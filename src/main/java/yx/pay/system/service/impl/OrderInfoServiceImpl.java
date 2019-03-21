package yx.pay.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.impl.BaseService;
import yx.pay.common.utils.WxUtil;
import yx.pay.system.dao.wx.OrderInfoMapper;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.domain.wx.OrderStatusEnum;
import yx.pay.system.domain.wx.PayTypeEnum;
import yx.pay.system.domain.wx.ProductInfo;
import yx.pay.system.service.OrderInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Slf4j
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

    @Override
    public List<OrderInfo> findOrderList(QueryRequest request, OrderInfo orderInfo) {
        try {
            if (request.getSortField() != null) {
                orderInfo.setSortField(request.getSortField());
                if (StringUtils.equals(FebsConstant.ORDER_DESC, request.getSortOrder()))
                    orderInfo.setSortOrder("desc");
                else
                    orderInfo.setSortOrder("asc");
            }
            return this.orderInfoMapper.findOrderList(orderInfo);
        } catch (Exception e) {
            log.error("查询流水异常", e);
            return new ArrayList<>();
        }
    }


}
