package yx.pay.system.dao.wx;

import yx.pay.system.domain.wx.ChannelInfo;

import java.util.List;

public interface ChannelInfoMapper {
    List<ChannelInfo> findChannelList(ChannelInfo channelInfo);
}
