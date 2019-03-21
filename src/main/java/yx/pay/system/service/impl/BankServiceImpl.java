package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.BankMapper;
import yx.pay.system.domain.Bank;
import yx.pay.system.service.BankService;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
@Component
public class BankServiceImpl extends BaseService<Bank> implements BankService {
    @Autowired
    private BankMapper mapper;

    @Override
    public List<Bank> selectByName(String name, int parentId) {
        return mapper.selectByName(name,parentId);
    }

    @Override
    public List<Bank> getParentBankList(int parentId) {
        return mapper.getParentBankList(parentId);
    }
}
