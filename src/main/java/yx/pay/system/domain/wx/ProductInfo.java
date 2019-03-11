package yx.pay.system.domain.wx;

public class ProductInfo {
    /**
     * 公众账号ID appid String(32) 是 wx8888888888888888 微信分配的公众账号ID
     * 用户标识 openid String(128) 是 o8GeHuLAsgefS_80exEr1cTqekUs 用户在商户appid下的唯一标识
     * 商户号 mch_id String(32) 是 1900000109 微信支付分配的商户号
     * 是否关注公众账号 is_subscribe String(1) 是 Y  用户是否关注公众账号，仅在公众账号类型支付有效，取值范围：Y或N;Y-关注;N-未关注
     * 随机字符串 nonce_str String(32) 是 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位。推荐随机数生成算法
     * 商品ID product_id String(32) 是 88888 商户定义的商品id 或者订单号
     * 签名 sign String(32) 是 C380BEC2BFD727A4B6845133519F3AD6 返回数据签名，签名生成算法
     */
    private String appid;
    private String openid;
    private String mch_id;
    private String is_subscribe;
    private String nonce_str;
    private String product_id;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
