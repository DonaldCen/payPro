package yx.pay.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.domain.FebsConstant;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.wx.MerchantMapper;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.service.MerchantService;

@Slf4j
@Service
public class MerchantServiceImpl extends BaseService<Merchant> implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public void updateMerchantQrUrl(Merchant merchant) {
        merchantMapper.updateMerchantQrUrl(merchant);
    }

    @Override
    public List<Merchant> findMerchantList(QueryRequest request, Merchant merchant) {
        try {
            if (request.getSortField() != null) {
                merchant.setSortField(request.getSortField());
                if (StringUtils.equals(FebsConstant.ORDER_ASC, request.getSortOrder()))
                    merchant.setSortOrder("asc");
                else if (StringUtils.equals(FebsConstant.ORDER_DESC, request.getSortOrder()))
                    merchant.setSortOrder("desc");
            }
            return this.merchantMapper.findMerchantList(merchant);
        } catch (Exception e) {
            log.error("查询商户异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Merchant findByName(String merchantName) {
        return this.merchantMapper.findByName(merchantName);
    }

    @Override
    public void addMerchant(@Valid Merchant merchant) {
        this.merchantMapper.addMerchant(merchant);
    }

    @Override
    public void updateMerchant(@Valid Merchant merchant) {
        this.merchantMapper.updateMerchant(merchant);
    }
}
