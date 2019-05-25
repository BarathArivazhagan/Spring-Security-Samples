package com.barath.app.entity;

import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
//@Table(name="USER_SECURITY")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="USER_ID")
    private Long userId;

    @Column(name="USER_NAME",unique = true,length = 50 ,nullable = false)
    private String userName;

    @Column(name="USER_PASSWORD",length = 1000,nullable = false)
    private String password;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
