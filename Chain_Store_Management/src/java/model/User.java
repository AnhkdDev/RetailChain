/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
package model;

import java.sql.Timestamp;

public class User {

    private int userID;
    private String username;
    private String fullName;
    private String email;
    private String passwordHash;
    private Timestamp lastLogin;
    private Integer roleID;
    private boolean isActive;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int userID, String username, String fullName, String email, String passwordHash,
                Timestamp lastLogin, Integer roleID, boolean isActive, Timestamp createdAt) {
        this.userID = userID;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
        this.roleID = roleID;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}