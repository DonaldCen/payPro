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

@Data
@ToString
@Table(name = "t_merchant")
public class Merchant extends BaseEntity {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "legal_person_name")
    private String legalPersonName;//法人名称
    @Column(name = "busi_license_num")
    private String busiLicenseNum;//营业执照号码
    @Column(name = "id_card_number")
    private String idCardNumber;//身份证
    @Column(name = "phone")
    private String phone;//手机号码
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "qr_code_url")
    private String qrCoreUrl;

}
