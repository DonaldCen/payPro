package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;
import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.ChannelMerchant;

@Component
public interface ChannelMerchantMapper extends MyMapper<ChannelMerchant> {
    Integer getIdByMerchantNo(String chlMerchanNo);
}
