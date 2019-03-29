package yx.pay.system.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.system.domain.vo.MerchantRegisterVo;
import yx.pay.system.domain.wx.Merchant;


public interface MerchantService extends IService<Merchant> {
    void updateMerchantQrUrl(Merchant merchant);

    List<Merchant> findMerchantList(QueryRequest request, Merchant merchant);

    Merchant findByName(String merchantName);

    void addMerchant(@Valid Merchant merchant);

    void updateMerchant(@Valid Merchant merchant);



    /**
     * 商户入驻申请, 返回 商户申请单号

     */
    FebsResponse merchantApply(MerchantRegisterVo vo);

}
