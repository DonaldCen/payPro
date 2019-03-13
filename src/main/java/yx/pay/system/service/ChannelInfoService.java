package yx.pay.system.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.system.domain.wx.ChannelInfo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface ChannelInfoService {
    List<ChannelInfo> findChannelList(QueryRequest request, ChannelInfo channelInfo);

    ChannelInfo findByName(String channelName);

    void addChannelInfo(ChannelInfo channelInfo);

    void updateChannelInfo(ChannelInfo channelInfo);
}
