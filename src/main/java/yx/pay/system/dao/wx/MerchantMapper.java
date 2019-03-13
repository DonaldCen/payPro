package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.Merchant;

@Component
public interface MerchantMapper extends MyMapper<Merchant> {
    void updateMerchantQrUrl(Merchant merchant);
}
