package com.ffi.api.kds.dto;

public class PrepareItemSupplyBaseRequest {
    private String billNo;
    private String posCode;
    private Long daySeq;
    private Integer itemSeq;
    
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    @Override
    public String toString() {
        return "PrepareItemSupplyBaseRequest [billNo=" + billNo + ", posCode=" + posCode + ", daySeq=" + daySeq
                + ", itemSeq=" + itemSeq + "]";
    }
    
}
