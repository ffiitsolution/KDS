package com.ffi.api.kds.dto;

import javax.validation.constraints.NotNull;

public class KdsHeaderRequest {
    @NotNull(message = "billNo is required")
    private String billNo;
    @NotNull(message = "kdsNo is required")
    private String kdsNo;
    @NotNull(message = "pocCode is required")
    private String posCode;
    @NotNull(message = "daySeq is required")
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
        return "KdsHeaderRequest [billNo=" + billNo + ", kdsNo=" + kdsNo + ", posCode=" + posCode + ", daySeq="
                + daySeq + "]";
    }
    
}
