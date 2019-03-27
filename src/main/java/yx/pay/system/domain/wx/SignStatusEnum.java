package yx.pay.system.domain.wx;

public enum SignStatusEnum {
    AUDITING("AUDITING", "审核中"),
    REJECTED("REJECTED", "已驳回"),
    FROZEN("FROZEN", "已冻结"),
    TO_BE_SIGNED("TO_BE_SIGNED", "待签约"),
    FINISH("FINISH", "完成")
    ;
    private String value;
    private String desc;

    private SignStatusEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
