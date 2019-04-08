package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.wx.ChannelMerchant;

public interface ChannelMerchantService extends IService<ChannelMerchant> {

    Integer getIdByMerchantNo(String chlMerchanNo);
}
