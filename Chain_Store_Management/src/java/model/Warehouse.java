package model;

public class Warehouse {
    private int warehouseID;
    private String warehouseName;

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

    @Override
    public String toString() {
        return "Warehouse{" + "warehouseID=" + warehouseID + ", warehouseName=" + warehouseName + '}';
    }
}