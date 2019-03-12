package yx.pay.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.QueryRequest;
import yx.pay.system.dao.wx.ChannelInfoMapper;
import yx.pay.system.domain.wx.ChannelInfo;
import yx.pay.system.service.ChannelInfoService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChannelInfoServiceImpl implements ChannelInfoService {
    @Autowired
    private ChannelInfoMapper channelInfoMapper;
    @Override
    public List<ChannelInfo> findChannelList(QueryRequest request, ChannelInfo channelInfo) {
        try {
            if (request.getSortField() != null) {
                channelInfo.setSortField(request.getSortField());
                if (StringUtils.equals(FebsConstant.ORDER_ASC, request.getSortOrder()))
                    channelInfo.setSortOrder("asc");
                else if (StringUtils.equals(FebsConstant.ORDER_DESC, request.getSortOrder()))
                    channelInfo.setSortOrder("desc");
            }
            return this.channelInfoMapper.findChannelList(channelInfo);
        } catch (Exception e) {
            log.error("查询渠道异常", e);
            return new ArrayList<>();
        }
    }
}
