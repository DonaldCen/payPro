package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;

import java.util.List;

import javax.validation.Valid;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.Merchant;

@Component
public interface MerchantMapper extends MyMapper<Merchant> {
    void updateMerchantQrUrl(Merchant merchant);

    List<Merchant> findMerchantList(Merchant merchant);

    Merchant findByName(String merchantName);

    void addMerchant(@Valid Merchant merchant);

    void updateMerchant(@Valid Merchant merchant);
}
