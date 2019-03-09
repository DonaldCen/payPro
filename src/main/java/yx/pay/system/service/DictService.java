package yx.pay.system.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.system.domain.Dict;

import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> findDicts(QueryRequest request, Dict dict);

    void createDict(Dict dict);

    void updateDict(Dict dicdt);

    void deleteDicts(String[] dictIds);

}
