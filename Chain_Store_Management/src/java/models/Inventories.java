package models;

public class Inventories {
    private int productID;
    private int warehouseID;
    private String productName;
    private String categoryName;
    private String size;
    private String color;
    private String warehouseName;
    private String storeName;
    private int quantity; 

    public Inventories() {
    }

    public Inventories(int productID, int warehouseID, String productName, String categoryName, String size, String color, String warehouseName, String storeName, int quantity) {
        this.productID = productID;
        this.warehouseID = warehouseID;
        this.productName = productName;
        this.categoryName = categoryName;
        this.size = size;
        this.color = color;
        this.warehouseName = warehouseName;
        this.storeName = storeName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}