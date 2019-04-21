package yx.pay.system.dao.wx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.validation.Valid;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.Merchant;

@Component
public interface MerchantMapper extends MyMapper<Merchant> {
    void updateMerchantQrUrl(Merchant merchant);

    List<Merchant> findMerchantList(@Param("merchant")Merchant merchant);

    Merchant findByName(String merchantName);

    void addMerchant(Merchant merchant);

    void updateMerchant(Merchant merchant);
}
