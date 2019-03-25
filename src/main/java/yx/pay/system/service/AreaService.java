package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.Area;

import java.util.List;

public interface AreaService extends IService<Area> {
    List<Area> selectAllProvince(int parentId);
}
