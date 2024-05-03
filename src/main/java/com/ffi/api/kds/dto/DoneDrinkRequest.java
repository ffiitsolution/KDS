package com.ffi.api.kds.dto;

public class DoneDrinkRequest {
    private String billNo;
    private String menuItemCode;
    private String posCode;
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
