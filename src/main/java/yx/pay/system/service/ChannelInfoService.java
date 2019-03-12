package yx.pay.system.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.system.domain.wx.ChannelInfo;

import java.util.List;

public interface ChannelInfoService {
    List<ChannelInfo> findChannelList(QueryRequest request, ChannelInfo channelInfo);
}
