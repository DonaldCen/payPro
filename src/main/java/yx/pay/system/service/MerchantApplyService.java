package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.wx.MerchantApply;

import java.util.List;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/29
 * @Version 1.0.0
 */
public interface MerchantApplyService extends IService<MerchantApply> {
    List<MerchantApply> findMerchantApplyList();
}
