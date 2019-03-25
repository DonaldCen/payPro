package yx.pay.system.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_area")
public class Area implements Serializable {
    @Column(name = "code")
    private Integer code;
    @Column(name = "area_name")
    private String areaName;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "order")
    private Integer order;
}
