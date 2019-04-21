package yx.pay.system.controller;

import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.service.MerchantApplyService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
        List<MerchantApply> list = this.merchantApplyService.findMerchantApplyList(request, merchantApply);
        return super.selectByPageNumSize(request, () -> list);
    }
    @PostMapping("excel")
    @RequiresPermissions("merchantApply:export")
    public void export(MerchantApply merchantApply, QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            List<MerchantApply> list = merchantApplyService.findMerchantApplyList(request, merchantApply);
            ExcelKit.$Export(MerchantApply.class, response).downXlsx(list, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }

    }
}
