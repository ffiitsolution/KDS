package com.ffi.api.kds.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class PrepareItemSupplyBaseRequest {
    @NotNull(message = "billNo is required")
    private String billNo;
    
    @NotNull(message = "posCode is required")
    private String posCode;
    
    @NotNull(message = "daySeq is required")
    private Long daySeq;
    
    @NotNull(message = "itemSeq is required")
    private Integer itemSeq;
    
    @NotNull(message = "itemDetailSeq is required")
    private Integer itemDetailSeq;
    
    @NotNull(message = "transDate is required")
    private Date transDate;
    
    public String getBillNo() {
        return billNo;
    }
    public Integer getItemDetailSeq() {
        return itemDetailSeq;
    }
    public void setItemDetailSeq(Integer itemDetailSeq) {
        this.itemDetailSeq = itemDetailSeq;
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
    public Date getTransDate() {
        return transDate;
    }
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }
    @Override
    public String toString() {
        return "PrepareItemSupplyBaseRequest [billNo=" + billNo + ", posCode=" + posCode + ", daySeq=" + daySeq
                + ", itemSeq=" + itemSeq + "]";
    }
    
}
