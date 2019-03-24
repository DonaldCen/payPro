package yx.pay.system.domain;

public enum ImageTypeEnum {
    /**
     * id_card_copy
     * 图像上传接口生成 media_id
     * id_card_national
     * 图像上传接口生成 media_idid_card_copy
     * 图像上传接口生成 media_id
     * id_card_national
     * 图像上传接口生成 media_id
     * store_entrance_pic
     * 门店门口照片，图像上传接口生成 media_id
     * indoor_pic
     * 店内环境照片，图像上传接口生成 media_id
     */
    ID_CARD_FRONT("id_card_copy",1),
    ID_CARD_BACK("id_card_national",2),
    STORE_ENTRANCE_PIC("store_entrance_pic",3),
    INDOOR_PIC("indoor_pic",4);
    private ImageTypeEnum(String typeName,int type){
        this.typeName = typeName;
        this.type = type;
    }
    public static String getTypeName(int type){
        switch (type){
            case 1:return "id_card_copy";
            case 2:return "id_card_national";
            case 3:return "store_entrance_pic";
            case 4:return "indoor_pic";
            default:return "other";
        }
    }
    private String typeName;
    private int type;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
