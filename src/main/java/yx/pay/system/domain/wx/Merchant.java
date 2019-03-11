package yx.pay.system.domain.wx;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Merchant {
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
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "qr_code_url")
    private String qrCoreUrl;

}
