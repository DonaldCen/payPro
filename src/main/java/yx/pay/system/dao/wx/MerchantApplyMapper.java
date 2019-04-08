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

    List<MerchantApply> findMerchantApplyList(MerchantApply merchantApply);

    MerchantApply findByName(String businessCode);

    /**
     * 新增商户入驻申请参数
     *
     * @param paramMap
     */
    void addMerchantApply(java.util.Map paramMap);

    /**
     * 更新申请入驻结果（成功，失败）
     *
     * @param paramMap
     */
    void updateMerchantApply(java.util.Map paramMap);

    Long getMerchantApplyNextID();

    int updateSubMchIdAndStatus(MerchantApply merchantApply);

    int updateStatus(MerchantApply merchantApply);

    List<String> getSubMchIdList(String finishStatus);
}
