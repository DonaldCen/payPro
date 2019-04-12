package yx.pay.system.domain.wx;

public enum WxPayTypeEnum {
    NATIVE(0,"NATIVE"),
    JSAPI(1,"JSAPI"),
    APP(2,"APP")
    ;
    private int type;
    private String typeName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private WxPayTypeEnum(int type,String typeName){
        this.type = type;
        this.typeName = typeName;
    }
}
