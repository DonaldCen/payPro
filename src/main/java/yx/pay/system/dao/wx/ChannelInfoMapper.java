package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.ChannelInfo;

import java.util.List;

@Component
public interface ChannelInfoMapper extends MyMapper<ChannelInfo> {
    List<ChannelInfo> findChannelList(ChannelInfo channelInfo);

    ChannelInfo findByName(String channelName);

    void addChannelInfo(ChannelInfo channelInfo);

    void updateChannelInfo(ChannelInfo channelInfo);
}
