package yx.pay.system.domain.wx;

import lombok.Data;
import lombok.ToString;
import yx.pay.system.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/11
 * @Version 1.0.0
 */
@Data
@ToString
@Table(name = "t_order")
public class OrderInfo extends BaseEntity {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private int id;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "pay_type")
    private Integer payType;
    @Column(name = "total_fee")
    private Double totalFee;//支付金额
    @Column(name = "order_no")
    private String orderNo;//订单流水号
    @Column(name = "pay_time")
    private Date payTime;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "status")
    private Integer status;//状态
    @Column(name = "prepay_id")
    private String prepayId;//微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "mch_no")
    private String mchNo;

}
