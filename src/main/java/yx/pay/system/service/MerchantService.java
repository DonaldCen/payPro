package yx.pay.system.service;

import yx.pay.system.domain.wx.Merchant;

public interface MerchantService {
    void updateMerchantQrUrl(Merchant merchant);
}
