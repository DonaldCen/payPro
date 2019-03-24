package yx.pay.system.domain.vo;


public class BankAreaVo {
    private String value;
    private String label;
    private boolean isLeaf;
    private Integer level;

    public BankAreaVo(){}

    public BankAreaVo(String value,String label,boolean isLeaf){
        this.value = value;
        this.label = label;
        this.isLeaf = isLeaf;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
        isLeaf = isleaf;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
