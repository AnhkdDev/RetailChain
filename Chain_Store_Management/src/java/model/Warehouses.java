package model;

public class Warehouses {
    private int warehouseID;
    private String warehouseName;
    private int storeID;
    private String storeName;

    public Warehouses() {
    }

    public Warehouses(int warehouseID, String warehouseName, int storeID, String storeName) {
        this.warehouseID = warehouseID;
        this.warehouseName = warehouseName;
        this.storeID = storeID;
        this.storeName = storeName;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    
    
}