package yx.pay.system.domain.wx;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@ToString
@Table(name = "t_chl_merchant")
public class ChannelMerchant implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private int id;

    @Column(name = "chl_merchant_no")
    private String chlMerchantNo;

    @Column(name = "chl_merchant_key")
    private String chlMerchantKey;

    private String notifyUrl;

    @Column(name = "status")
    private Integer status;
}
