package yx.pay.system.domain.wx;

public enum OrderPayTypeEnum {
    CREATE_QR_CODE(1,"wx"),
    ;
    private int index;
    private String value;

    private OrderPayTypeEnum(int index, String value){
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
