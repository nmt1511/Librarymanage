package com.example.librarymanage.entities;

public class User {

    private int userId;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String studentCode;
    private String username;
    private String password;
    private int role;

    // Constructor
    public User(int userId, String name, String gender, String phone, String email, String studentCode,
                String username, String password, int role) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.studentCode = studentCode;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name; // Để hiển thị tên trong ListView
    }
}
