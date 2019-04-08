package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.wx.ChannelMerchantMapper;
import yx.pay.system.domain.wx.ChannelMerchant;
import yx.pay.system.service.ChannelMerchantService;

@Service
public class ChannelMerchantServiceImpl extends BaseService<ChannelMerchant> implements ChannelMerchantService {
    @Autowired
    private ChannelMerchantMapper channelMerchantMapper;

    @Override
    public Integer getIdByMerchantNo(String chlMerchanNo) {
        return channelMerchantMapper.getIdByMerchantNo(chlMerchanNo);
    }
}
