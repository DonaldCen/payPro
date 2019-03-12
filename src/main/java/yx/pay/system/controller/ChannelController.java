package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.system.domain.wx.ChannelInfo;
import yx.pay.system.service.ChannelInfoService;

import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("channel")
public class ChannelController extends BaseController {
    @Autowired
    private ChannelInfoService channelInfoService;

    @GetMapping
    @RequiresPermissions("channel:view")
    public Map<String, Object> channelList(QueryRequest request, ChannelInfo channelInfo){
        return super.selectByPageNumSize(request, () -> this.channelInfoService.findChannelList(request,channelInfo));
    }

}
