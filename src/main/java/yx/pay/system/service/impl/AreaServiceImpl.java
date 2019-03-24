package yx.pay.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yx.pay.common.service.impl.BaseService;
import yx.pay.system.dao.AreaMapper;
import yx.pay.system.domain.Area;
import yx.pay.system.service.AreaService;

import java.util.List;

@Service
public class AreaServiceImpl extends BaseService<Area> implements AreaService {
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Area> selectAllProvince(int parentId) {
        return areaMapper.selectAllProvince(parentId);
    }
}
