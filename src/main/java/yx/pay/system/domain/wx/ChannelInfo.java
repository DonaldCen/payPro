package yx.pay.system.domain.wx;

import lombok.Data;
import lombok.ToString;
import yx.pay.system.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Table(name = "t_channel")
public class ChannelInfo extends BaseEntity{
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private int id;
    @Column(name = "channel_code")
    private String channelCode;//渠道编码
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

}
