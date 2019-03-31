package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.service.MerchantApplyService;

import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/sys/merchantApply")
public class MerchantApplyController extends BaseController {
    @Autowired
    private MerchantApplyService merchantApplyService;

    @GetMapping
    @RequiresPermissions("merchantApply:view")
    public Map<String, Object> merchantList(QueryRequest request, MerchantApply merchantApply) {
        return super.selectByPageNumSize(request, () -> this.merchantApplyService.findMerchantApplyList(request, merchantApply));
    }
}
