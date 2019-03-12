package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yx.pay.system.dao.wx.MerchantMapper;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.service.MerchantService;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public void updateMerchantQrUrl(Merchant merchant) {
        merchantMapper.updateMerchantQrUrl(merchant);
    }
}
