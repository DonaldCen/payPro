package yx.pay.system.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.system.domain.wx.ChannelInfo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface ChannelInfoService extends IService<ChannelInfo> {
    List<ChannelInfo> findChannelList(QueryRequest request, ChannelInfo channelInfo);

    ChannelInfo findByName(String channelName);

    void addChannelInfo(ChannelInfo channelInfo);

    void updateChannelInfo(ChannelInfo channelInfo);
}
