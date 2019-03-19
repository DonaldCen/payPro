package yx.pay.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.annotation.Log;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.User;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.service.MerchantService;
import yx.pay.system.service.UserService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/14
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/merchant")
public class MerchantController extends BaseController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;

    @GetMapping
    @RequiresPermissions("merchant:view")
    public Map<String, Object> merchantList(QueryRequest request, Merchant merchant){
        return super.selectByPageNumSize(request, () -> this.merchantService.findMerchantList(request,merchant));
    }

    @GetMapping("check/{merchantName}")
    public boolean checkMerchantName(@NotBlank(message = "{required}") @PathVariable String merchantName) {
        return this.merchantService.findByName(merchantName) == null;
    }

    @Log("新增商户")
    @PostMapping
    @RequiresPermissions("merchant:add")
    public void addUser(@Valid Merchant merchant,@Valid User user) throws FebsException {
        try {
            int userId = this.userService.addUserAndReturnId(user);
            this.merchantService.addMerchant(merchant);
        } catch (Exception e) {
            log.error("新增商户失败", e);
            throw new FebsException("新增商户失败");
        }
    }

    @Log("修改商户")
    @PutMapping
    @RequiresPermissions("merchant:update")
    public void updateUser(@Valid Merchant merchant) throws FebsException {
        try {
            this.merchantService.updateMerchant(merchant);
        } catch (Exception e) {
            String message = "修改商户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
