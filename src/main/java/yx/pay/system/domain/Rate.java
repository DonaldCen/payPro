package yx.pay.system.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
@Data
@Table(name = "t_rate")
public class Rate implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;
    @Column(name = "rate")
    private String rate;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "remark")
    private String remark;

}
