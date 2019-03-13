package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yx.pay.common.annotation.Log;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.User;
import yx.pay.system.domain.wx.ChannelInfo;
import yx.pay.system.service.ChannelInfoService;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @GetMapping("check/{channelName}")
    public boolean checkChannelName(@NotBlank(message = "{required}") @PathVariable String channelName) {
        return this.channelInfoService.findByName(channelName) == null;
    }

    @Log("新增通道")
    @PostMapping
    @RequiresPermissions("channel:add")
    public void addUser(@Valid ChannelInfo channelInfo) throws FebsException {
        try {
            this.channelInfoService.addChannelInfo(channelInfo);
        } catch (Exception e) {
            log.error("新增通道失败", e);
            throw new FebsException("新增通道失败");
        }
    }

    @Log("修改通道")
    @PutMapping
    @RequiresPermissions("channel:update")
    public void updateUser(@Valid ChannelInfo channelInfo) throws FebsException {
        try {
            this.channelInfoService.updateChannelInfo(channelInfo);
        } catch (Exception e) {
            String message = "修改通道失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
