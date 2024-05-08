package com.ffi.api.kds.dto;

import javax.validation.constraints.NotNull;

public class DoneDrinkRequest {
    @NotNull(message = "billNo is required")
    private String billNo;
    @NotNull(message = "menuItemCode is required")
    private String menuItemCode;
    @NotNull(message = "posCode is required")
    private String posCode;
    @NotNull(message = "transType is required")
    private String transType;
    
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getMenuItemCode() {
        return menuItemCode;
    }
    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }
    public String getPosCode() {
        return posCode;
    }
    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }
    public String getTransType() {
        return transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
}
