/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.security.Timestamp;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class LoyaltyCustomer {
    private int loyaltyCustomerID;
private int customerID;
private double loyaltyPoints;
private String tierLevel;
private Date joinDate;
private Timestamp lastUpdated;
private boolean isActive;

// Getters and Setters
public int getLoyaltyCustomerID() {
    return loyaltyCustomerID;
}

public void setLoyaltyCustomerID(int loyaltyCustomerID) {
    this.loyaltyCustomerID = loyaltyCustomerID;
}

public int getCustomerID() {
    return customerID;
}

public void setCustomerID(int customerID) {
    this.customerID = customerID;
}

public double getLoyaltyPoints() {
    return loyaltyPoints;
}

public void setLoyaltyPoints(double loyaltyPoints) {
    this.loyaltyPoints = loyaltyPoints;
}

public String getTierLevel() {
    return tierLevel;
}

public void setTierLevel(String tierLevel) {
    this.tierLevel = tierLevel;
}

public Date getJoinDate() {
    return joinDate;
}

public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
}

public Timestamp getLastUpdated() {
    return lastUpdated;
}

public void setLastUpdated(Timestamp lastUpdated) {
    this.lastUpdated = lastUpdated;
}

public boolean getIsActive() {
    return isActive;
}

public void setIsActive(boolean isActive) {
    this.isActive = isActive;
}
}
