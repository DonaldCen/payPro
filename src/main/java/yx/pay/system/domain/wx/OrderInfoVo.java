package yx.pay.system.domain.wx;

import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/29
 * @Version 1.0.0
 */
public class OrderInfoVo {
    /**
     * 公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID
     * 商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
     * 子商户公众账号ID	sub_appid	否	String(32)	wx8888888888888888	微信分配的子商户公众账号ID，如需在支付完成后获取sub_openid则此参数必传。
     * 子商户号	sub_mch_id	是	String(32)	1900000109	微信支付分配的子商户号
     * 设备号	device_info	否	String(32)	013467007045764	终端设备号(门店号或收银设备ID)，注意：PC网页或JSAPI支付请传"WEB"
     * 开发票入口开放标识	receipt	否	String(8)	Y	Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     * 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
     * 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
     * 签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     * 商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
     * 商品描述交易字段格式根据不同的应用场景建议按照以下格式上传：
     *
     * （1）PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
     *
     * （2） 公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
     *
     * （3） H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
     *
     * （4） 线下门店——门店品牌名-城市分店名-实际商品名称，例如： image形象店-深圳腾大- QQ公仔）
     *
     * （5） APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     *
     * 商品详情	detail	否	String(6000)	 	商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
     * 附加数据	attach	否	String(127)	说明	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     * 商户订单号	out_trade_no	是	String(32)	1217752501201407033233368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
     * 货币类型	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * 总金额	total_fee	是	Int	888	订单总金额，只能为整数，详见支付金额
     * 终端IP	spbill_create_ip	是	String(16)	123.12.12.123	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
     * 交易起始时间	time_start	否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     * 交易结束时间	time_expire	否	String(14)	20091227091010
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
     *
     * 建议：最短失效时间间隔大于1分钟
     *
     * 订单优惠标记	goods_tag	否	String(32)	WXG	订单优惠标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
     * 通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     * 交易类型	trade_type	是	String(16)	JSAPI
     * JSAPI -JSAPI支付
     *
     * NATIVE -Native支付
     *
     * APP -APP支付
     *
     * 说明详见参数规定
     */

    /**
     * 腾讯充值中心-QQ会员充值
     * 商品描述交易字段格式根据不同的应用场景建议按照以下格式上传：
     *
     * （1）PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
     *
     * （2） 公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
     *
     * （3） H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
     *
     * （4） 线下门店——门店品牌名-城市分店名-实际商品名称，例如： image形象店-深圳腾大- QQ公仔）
     *
     * （5） APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     *
     */
    @NotBlank(message = "{required}")
    private String body;//String(128)
    @NotBlank(message = "{required}")
    private Integer total_fee;//总金额，单位为分
    private String chlMerchanNo;//商家编号
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    private String ip;

    public String getIp() {
        return ip;
    }

    public String getChlMerchanNo() {
        return chlMerchanNo;
    }

    public void setChlMerchanNo(String chlMerchanNo) {
        this.chlMerchanNo = chlMerchanNo;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }
}
