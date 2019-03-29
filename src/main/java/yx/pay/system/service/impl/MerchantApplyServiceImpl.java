package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.wx.MerchantApplyMapper;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.service.MerchantApplyService;

import java.util.List;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/29
 * @Version 1.0.0
 */
@Service
public class MerchantApplyServiceImpl extends BaseService<MerchantApply> implements MerchantApplyService{
    @Autowired
    private MerchantApplyMapper  merchantApplyMapper;
    public List<MerchantApply> findMerchantApplyList(){
        return merchantApplyMapper.findMerchantApplyList();
    }

}
