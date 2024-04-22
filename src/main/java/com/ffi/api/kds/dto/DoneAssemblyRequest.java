package com.ffi.api.kds.dto;

public class DoneAssemblyRequest {
    private String billNo;
    private String kdsNo;
    private String posCode;
    private Long daySeq;
    
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
    @Override
    public String toString() {
        return "DoneAssemblyRequest [billNo=" + billNo + ", kdsNo=" + kdsNo + ", posCode=" + posCode + ", daySeq="
                + daySeq + "]";
    }
    
}
