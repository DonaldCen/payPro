package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yx.pay.common.annotation.Log;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.wx.ChannelInfo;
import yx.pay.system.domain.wx.OrderInfo;
import yx.pay.system.service.ChannelInfoService;
import yx.pay.system.service.OrderInfoService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/sys/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping
    @RequiresPermissions("order:view")
    public Map<String, Object> orderList(QueryRequest request, OrderInfo orderInfo){
        return super.selectByPageNumSize(request, () -> this.orderInfoService.findOrderList(request,orderInfo));
    }

}