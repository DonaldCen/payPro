package yx.pay.system.controller;

import com.wuwenze.poi.ExcelKit;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import yx.pay.common.annotation.Log;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.domain.QueryRequest;
import yx.pay.common.exception.FebsException;
import yx.pay.system.domain.User;
import yx.pay.system.domain.vo.MerchantRegisterVo;
import yx.pay.system.domain.wx.Merchant;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.service.MerchantService;
import yx.pay.system.service.UploadService;
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
    @Autowired
    private UploadService uploadService;//图片上传服务


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
    /**
     * 上传小微商户图片,获取上传图片的media_id(如上传成功)
     *
     * @param multipartFile
     * @return
     */
    @Log("上传图片")
    @PostMapping("/upload")
    public FebsResponse upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws FebsException{
        log.info("图片大小 {}", multipartFile.getSize());
        try {
            return uploadService.uploadFile(multipartFile);
        } catch (Exception e) {
            log.error("upload error..",e);
        }
        return new FebsResponse().fail("upload fail..");
    }

    /**
     * 商户入驻申请
     *
     */
    @Log("商户入驻申请")
    @PostMapping("/merchantApply")
    public FebsResponse merchantApply(MerchantRegisterVo vo) throws FebsException{
        return merchantService.merchantApply(vo);
    }

}
