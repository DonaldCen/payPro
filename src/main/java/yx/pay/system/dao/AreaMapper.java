package yx.pay.system.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Area;

import java.util.List;

@Component
public interface AreaMapper extends MyMapper<Area> {
    List<Area> selectAllProvince(@Param("parentId")int parentId);
}
