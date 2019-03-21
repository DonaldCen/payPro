package yx.pay.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.FebsResponse;
import yx.pay.system.domain.Bank;
import yx.pay.system.service.BankService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/common")
public class CommonController extends BaseController {
    @Autowired
    private BankService bankService;

    @GetMapping("getParentBankList")
    public FebsResponse getParentBankList(){
        List<Bank> list = bankService.getParentBankList(0);
        return new FebsResponse().success(list);
    }

    @PostMapping("getBankListByName")
    public List<Bank> getBankListByName(String name,int parentId){
        List<Bank> list = bankService.selectByName(name,parentId);
        return list;
    }
}
