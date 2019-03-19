package yx.pay.system.domain.wx;

public enum PayTypeEnum {
    WECHAT_PAY(0,"微信"),
    ALI_PAY(1,"支付宝");
    private int type;
    private String name;

    private PayTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
