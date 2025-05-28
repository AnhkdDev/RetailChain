package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Purchase {
    private int purchaseID;
    private Timestamp purchaseDate;
    private BigDecimal totalAmount;
    private String supplierName;
    private String warehouseName;

    public Purchase(int purchaseID, Timestamp purchaseDate, BigDecimal totalAmount, String supplierName, String warehouseName) {
        this.purchaseID = purchaseID;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.supplierName = supplierName;
        this.warehouseName = warehouseName;
    }

    
    
    
   
    
    // Getters and Setters
    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Override
    public String toString() {
        return "Purchase{" + "purchaseID=" + purchaseID + ", purchaseDate=" + purchaseDate + ", totalAmount=" + totalAmount + ", supplierName=" + supplierName + ", warehouseName=" + warehouseName + '}';
    }
    
    
}