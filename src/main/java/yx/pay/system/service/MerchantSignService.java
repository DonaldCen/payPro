package yx.pay.system.service;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import yx.pay.common.annotation.Log;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.common.service.IService;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.domain.wx.MerchantApply;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


public interface MerchantSignService extends IService<MerchantApply> {

    /**
     * 小微商户签约申请
     * @param applymentId
     * @return
     */
    FebsResponse merchantSignApply(String applymentId);

    /**
     * 小微商户签约状态更新
     * @param applymentId
     * @return
     */
    FebsResponse merchantSignUpStatus(String applymentId);


}
