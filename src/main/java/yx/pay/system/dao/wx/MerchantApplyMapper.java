package yx.pay.system.dao.wx;

import org.springframework.stereotype.Component;
import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.wx.MerchantApply;

import java.util.List;

/**
 * Created by 0151717 on 2019/3/26.
 */

@Component
public interface MerchantApplyMapper extends MyMapper<MerchantApply> {
    List<MerchantApply> findChannelList(MerchantApply merchantApply);
    MerchantApply findByName(String businessCode);
    void addMerchantApply(MerchantApply merchantApply);
    void updateMerchantApply(MerchantApply merchantApply);
}
