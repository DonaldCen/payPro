package yx.pay.system.domain.wx;

import lombok.Data;
import lombok.ToString;
import yx.pay.system.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 0151717 on 2019/3/26.
 */
@Data
@ToString
@Table(name = "t_merchant_apply")
public class MerchantApply extends BaseEntity {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    @Column(name = "version")
    private String version;//1、接口版本号
    @Column(name = "cert_sn")
    private String certSN;//2、平台证书序列号
    @Column(name = "mch_id")
    private String mchID;//3、商户号
    @Column(name = "nonce_str")
    private String nonceStr;//4、随机字符串
    @Column(name = "sign_type")
    private String signType;//5、签名类型
    @Column(name = "sign")
    private String sign;//6、签名
    @Column(name = "business_code")
    private String businessCode;//7、业务申请编号
    @Column(name = "id_card_copy")//8、身份证人像面照片
    private String idCardCopy;
    @Column(name = "id_card_nationa")
    private Integer idCardNationa;//9、身份证国徽面照片
    @Column(name = "id_card_name")
    private String idCardName;//10、身份证姓名
    @Column(name = "id_card_number")
    private String idCardNumber;//11、身份证号码
    @Column(name = "id_card_valid_time")
    private String idCardValidTime;//12、身份证有效期限
    @Column(name = "account_name")
    private String accountName;//13、开户名称
    @Column(name = "account_bank")
    private String accountBank;//14、开户银行
    @Column(name = "bank_address_code")
    private String bankAddressCode;//15、开户银行省市编码
    @Column(name = "bank_name")
    private String bankName;//16、开户银行全称（含支行）
    @Column(name = "account_number")
    private String accountNumber;//17、银行账号
    @Column(name = "store_name")
    private String storeName;//18、门店名称
    @Column(name = "store_address_code")
    private String storeAddressCode;//19、门店省市编码
    @Column(name = "store_street")
    private String storeStreet;//20、门店街道名称
    @Column(name = "store_longitude")
    private String storeLogitude;//21、门店经度
    @Column(name = "store_latitude")
    private String storeLatitude;//22、门店纬度
    @Column(name = "store_entrance_pic")
    private String storeEntrancePic;//23、门店门口照片
    @Column(name = "indoor_pic")
    private String indoorPic;//24、店内环境照片
    @Column(name = "address_certification")
    private String addressCertification;//25、经营场地证明
    @Column(name = "merchant_shortname")
    private String merchantShortName;//26、商户简称
    @Column(name = "service_phone")
    private String servicePhone;//27、客服电话
    @Column(name = "product_desc")
    private String productDesc;//28、售卖商品/提供服务描述
    @Column(name = "rate")
    private String rate;//29、费率
    @Column(name = "business_addition_desc")
    private String businessAdditionDesc;//30、补充说明
    @Column(name = "business_addition_pics")
    private String businessAdditionPics;//31、补充材料
    @Column(name = "contact")
    private String contact;//32、联系人姓名
    @Column(name = "contact_phone") //33、手机号码
    private String contactPhone;
    @Column(name = "contact_email")
    private String contactEmail;//34、联系邮箱
    @Column(name = "applyment_id")
    private String applymentID;//35、商户申请单号
    @Column(name = "create_date")
    private String createDate;//36、创建日期
    @Column(name = "apply_date")
    private String applyDate;//37、申请日期
    @Column(name = "update_date")
    private String updateDate;//38、更新日期
    @Column(name = "sub_mch_id")
    private String subMchId;//39、小微商户id
    @Column(name = "status")
    private String status;//40、申请状态
    @Column(name="bank_id")
    private String bank_id;//41、银行ID
    @Column(name="rate_id")
    private String rate_id;//42、费率ID
    @Column(name="data_status")
    private String data_status;//43、数据状态{0 :生效 1：失效}
    @Column(name="apply_desc")
    private String apply_desc;//43、申请说明{ 签名失效、敏感数据加密失效、认证失效、网络失效。。。}



}
