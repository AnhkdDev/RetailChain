package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents a customer in the Shop database.
 * Matches the structure of the Customers table and includes additional fields for display purposes.
 * @author Admin
 */
public class Customer {
    private int customerID; 
    private String fullName; 
    private String phone; 
    private String address;
    private String gender; 
    private Date birthDate; 
    private Timestamp createdAt; 
    private boolean isActive;
    private int userId; 
    private String img;
    private String gmail; 
    private double totalSpent; 
    private String membershipLevel;

    // Default constructor
    public Customer() {
    }

    // Parameterized constructor
    public Customer(int customerID, String fullName, String phone, String address, String gender, Date birthDate, 
                    Timestamp createdAt, boolean isActive, int userId, String img, String gmail, 
                    double totalSpent, String membershipLevel) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.userId = userId;
        this.img = img;
        this.gmail = gmail;
        this.totalSpent = totalSpent;
        this.membershipLevel = membershipLevel;
    }

    // Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }
}