package model;

public class Store {
    private String storeID;
    private String storeName;
    private String address;
    private String phoneNumber;
    private String email;
    private boolean active;

    // Default constructor
    public Store() {
    }

    // Constructor for creating a store with minimal fields
    public Store(String storeName, String address, String phoneNumber, String email, boolean active) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.active = active;
    }

    // Getters and setters
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Store{" +
               "storeID='" + storeID + '\'' +
               ", storeName='" + storeName + '\'' +
               ", address='" + address + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", active=" + active +
               '}';
    }
}