package yx.pay.system.dao.wx;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.Merchant;

public interface MerchantMapper extends MyMapper<Merchant> {
    void updateMerchantQrUrl(Merchant merchant);
}
