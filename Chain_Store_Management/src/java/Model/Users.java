/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author nqagh
 */
public class Users {
 
    private int userId;


    private String username;
    private String FullName;
    private String email;

  
    private String passwordHash;

    private Date lastLogin;


    private Integer roleId;
    private Boolean isActive;
    private Date createdAt;

    // Constructors
    public Users() {
    }

    public Users(int userId, String username, String FullName, String email, String passwordHash, Date lastLogin, Integer roleId, Boolean isActive, Date createdAt) {
        this.userId = userId;
        this.username = username;
        this.FullName = FullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
        this.roleId = roleId;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
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

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

  

    // toString (optional, for debugging)
    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", lastLogin=" + lastLogin +
                ", roleId=" + roleId +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
