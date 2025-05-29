package model;

public class Store {
    private String storeID;
    private String storeName;
    private String address;
    private String phoneNumber; // Matches JSP, but maps to database column 'phone'
    private String email;
    private boolean isActive;

    public Store() {
    }

    public Store(String storeName, String address, String phoneNumber, String email, boolean isActive) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isActive = isActive;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}