package yx.pay.system.domain.wx;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChannelInfo implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;
    @Column(name = "channel_code")
    private String channelCode;//通道编码
    @Column(name = "channel_name")
    private String channelName;
    @Column(name = "trade_type")
    private Integer tradeType;//交易类型
    @Column(name = "channel_cost_rate")
    private Double chanelCostRate;//通道成本利率，百分比
    @Column(name = "channel_cost")
    private Double channelCost;//通道成本费用，单笔费用
    @Column(name = "settlement_type")
    private Integer settlementType;//结算类型 0-D0 1-T+0 2-T+1
    @Column(name = "status")
    private Integer status;//通道状态 0：可用;1:禁用
    @Column(name = "pay_limit")
    private Double payLimit;//支付限额
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

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
