package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.Test;

import java.util.List;

public interface TestService extends IService<Test> {

    List<Test> findTests();

    /**
     * 批量插入
     * @param list List<Test>
     */
    void batchInsert(List<Test> list);
}
