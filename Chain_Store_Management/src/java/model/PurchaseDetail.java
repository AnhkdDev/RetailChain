package model;

import java.math.BigDecimal;

public class PurchaseDetail {
    private int purchaseID;
    private int productID;
    private String productName;
    private int quantity;
    private BigDecimal costPrice;

    // Constructor
    public PurchaseDetail(int purchaseID, int productID, String productName, int quantity, BigDecimal costPrice) {
        this.purchaseID = purchaseID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.costPrice = costPrice;
    }

    // Getters and Setters
    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
}