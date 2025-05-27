package models;

public class Stores {

    private int storeID;
    private String storeName;


    public Stores() {
    }

    public Stores(int storeID, String storeName) {
        this.storeID = storeID;
        this.storeName = storeName;
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
