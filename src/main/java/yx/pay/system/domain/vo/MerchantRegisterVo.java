package yx.pay.system.domain.vo;

import javax.validation.constraints.NotNull;

public class MerchantRegisterVo {
    /**
     * 业务申请编号
     */
    private String business_code;
    /**
     * 身份证人像面照片 media_id
     */
    @NotNull(message = "身份证人像面照片不能为空")
    private String id_card_copy;
    /**
     * 身份证国徽面照片 media_id
     */
    @NotNull(message = "身份证国徽面照片不能为空")
    private String id_card_national;
    /**
     * 身份证姓名
     */
    @NotNull(message = "身份证姓名不能为空")
    private String id_card_name;
    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    private String id_card_number;
    /**
     * 身份证有效期限
     * 格式：
     * ["1970-01-01","长期"]
     */
    @NotNull(message = "身份证有效期限不能为空")
    private String id_card_valid_time;
    /**
     * 开户名称
     */
    @NotNull(message = "开户名称不能为空")
    private String account_name;
    /**
     * 开户银行
     */
    private String account_bank;
    /**
     * 开户银行省市编码
     */
    @NotNull(message = "开户银行省市编码不能为空")
    private String bank_address_code;
    /**
     * 开户银行全称（含支行）
     */
    @NotNull(message = "开户银行全称（含支行）不能为空")
    private String bank_name;
    /**
     * 银行账号
     */
    @NotNull(message = "银行账号不能为空")
    private String account_number;
    /**
     * 门店名称
     */
    @NotNull(message = "门店名称不能为空")
    private String store_name;
    /**
     * 门店省市编码
     */
    @NotNull(message = "门店省市编码不能为空")
    private String store_address_code;
    /**
     * 门店街道名称
     */
    @NotNull(message = "门店街道名称不能为空")
    private String store_street;
    /**
     * 门店经度
     */
    private String store_longitude;
    /**
     * 门店纬度
     */
    private String store_latitude;
    /**
     * 门店门口照片 media_id
     */
    @NotNull(message = "门店门口照片不能为空")
    private String store_entrance_pic;
    /**
     * 店内环境照片 media_id
     */
    @NotNull(message = "店内环境照片不能为空")
    private String indoor_pic;
    /**
     * 经营场地证明
     */
    private String address_certification;
    private String business_addition_desc;
    private String business_addition_pics;
    /**
     * 商户简称
     */
    @NotNull(message = "商户简称不能为空")
    private String merchant_shortname;
    /**
     * 客服电话
     */
    @NotNull(message = "客服电话不能为空")
    private String service_phone;
    /**
     * 售卖商品/提供服务描述
     */
    @NotNull(message = "售卖商品/提供服务描述不能为空")
    private String product_desc;
    /**
     * 费率
     */
    private String rate;
    /**
     * 联系人姓名
     */
    @NotNull(message = "联系人姓名不能为空")
    private String contact;
    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    private String contact_phone;
    /**
     * 银行ID
     */
    @NotNull(message = "银行不能为空")
    private Integer bankId;
    /**
     * 费率ID
     */
    @NotNull(message = "费率不能为空")
    private Integer rateId;

    private String contact_email;

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getId_card_copy() {
        return id_card_copy;
    }

    public void setId_card_copy(String id_card_copy) {
        this.id_card_copy = id_card_copy;
    }

    public String getId_card_national() {
        return id_card_national;
    }

    public void setId_card_national(String id_card_national) {
        this.id_card_national = id_card_national;
    }

    public String getId_card_name() {
        return id_card_name;
    }

    public void setId_card_name(String id_card_name) {
        this.id_card_name = id_card_name;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getId_card_valid_time() {
        return id_card_valid_time;
    }

    public void setId_card_valid_time(String id_card_valid_time) {
        this.id_card_valid_time = id_card_valid_time;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_bank() {
        return account_bank;
    }

    public void setAccount_bank(String account_bank) {
        this.account_bank = account_bank;
    }

    public String getBank_address_code() {
        return bank_address_code;
    }

    public void setBank_address_code(String bank_address_code) {
        this.bank_address_code = bank_address_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address_code() {
        return store_address_code;
    }

    public void setStore_address_code(String store_address_code) {
        this.store_address_code = store_address_code;
    }

    public String getStore_street() {
        return store_street;
    }

    public void setStore_street(String store_street) {
        this.store_street = store_street;
    }

    public String getStore_entrance_pic() {
        return store_entrance_pic;
    }

    public void setStore_entrance_pic(String store_entrance_pic) {
        this.store_entrance_pic = store_entrance_pic;
    }

    public String getIndoor_pic() {
        return indoor_pic;
    }

    public void setIndoor_pic(String indoor_pic) {
        this.indoor_pic = indoor_pic;
    }

    public String getMerchant_shortname() {
        return merchant_shortname;
    }

    public void setMerchant_shortname(String merchant_shortname) {
        this.merchant_shortname = merchant_shortname;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(String store_longitude) {
        this.store_longitude = store_longitude;
    }

    public String getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(String store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getAddress_certification() {
        return address_certification;
    }

    public void setAddress_certification(String address_certification) {
        this.address_certification = address_certification;
    }

    public String getBusiness_addition_desc() {
        return business_addition_desc;
    }

    public void setBusiness_addition_desc(String business_addition_desc) {
        this.business_addition_desc = business_addition_desc;
    }

    public String getBusiness_addition_pics() {
        return business_addition_pics;
    }

    public void setBusiness_addition_pics(String business_addition_pics) {
        this.business_addition_pics = business_addition_pics;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }
}
