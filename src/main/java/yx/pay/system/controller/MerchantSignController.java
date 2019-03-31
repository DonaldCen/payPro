package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yx.pay.common.annotation.Log;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.User;
import yx.pay.system.domain.vo.MerchantRegisterVo;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.service.MerchantService;
import yx.pay.system.service.MerchantSignService;
import yx.pay.system.service.UploadService;
import yx.pay.system.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/14
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/merchantSign")
public class MerchantSignController extends BaseController {
    @Autowired
    private MerchantSignService merchantSignServiceImpl;

    /**
     * 小微商户签约申请
     */
//    @Log("小微商户签约申请")
    @PostMapping("/apply")
//    @RequiresPermissions("merchantSign:apply")
    public FebsResponse merchantSignApply(String applymentId) throws FebsException {
        return merchantSignServiceImpl.merchantSignApply(applymentId);
    }

    /**
     * 小微商户签约状态更新
     */
//    @Log("小微商户签约状态更新")
    @PostMapping("/upStatus")
//    @RequiresPermissions("merchantSign:upStatus")
    public FebsResponse merchantSignUpStatus(String applymentId) throws FebsException {
        return merchantSignServiceImpl.merchantSignUpStatus(applymentId);
    }
}
