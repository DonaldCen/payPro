package yx.pay.system.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Bank;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
@Component
public interface BankMapper extends MyMapper<Bank>{
    List<Bank> selectByName(@Param("bankName")String name, @Param("parentId")int parentId);

    List<Bank> getParentBankList(@Param("parentId")int parentId);
}
