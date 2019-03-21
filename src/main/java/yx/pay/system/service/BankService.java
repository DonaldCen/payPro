package yx.pay.system.service;

import java.util.List;

import yx.pay.common.service.IService;
import yx.pay.system.domain.Bank;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
public interface BankService extends IService<Bank> {

    List<Bank> selectByName(String name, int parentId);

    List<Bank> getParentBankList(int i);
}
