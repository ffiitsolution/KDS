package com.ffi.api.kds.dto;

public class PrepareItemSupplyBaseRequest {
    private String billNo;
    private String kdsNo;
    private String posCode;
    private Long daySeq;
    private Integer itemSeq;
    private String menuItemCode;
    
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getKdsNo() {
        return kdsNo;
    }
    public void setKdsNo(String kdsNo) {
        this.kdsNo = kdsNo;
    }
    public String getPosCode() {
        return posCode;
    }
    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }
    public Long getDaySeq() {
        return daySeq;
    }
    public void setDaySeq(Long daySeq) {
        this.daySeq = daySeq;
    }
    public Integer getItemSeq() {
        return itemSeq;
    }
    public void setItemSeq(Integer itemSeq) {
        this.itemSeq = itemSeq;
    }
    public String getMenuItemCode() {
        return menuItemCode;
    }
    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }
    @Override
    public String toString() {
        return "PrepareItemAssembyRequest [billNo=" + billNo + ", kdsNo=" + kdsNo + ", posCode=" + posCode + ", daySeq="
                + daySeq + ", itemSeq=" + itemSeq + ", menuItemCode=" + menuItemCode + "]";
    }
    
    
}
