package yx.pay.system.domain;

import java.io.Serializable;

import javax.persistence.Transient;

import lombok.Data;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/14
 * @Version 1.0.0
 */
@Data
public class BaseEntity implements Serializable {
    // 排序字段
    @Transient
    private String sortField;

    // 排序规则 ascend 升序 descend 降序
    @Transient
    private String sortOrder;

    @Transient
    private String createTimeFrom;
    @Transient
    private String createTimeTo;
}
