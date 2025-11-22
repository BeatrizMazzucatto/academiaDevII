package com.academiadev.domain.entities;

public class Admin extends User {
    
    public Admin(String name, String email) {
        super(name, email);
    }
    
    @Override
    public boolean isAdmin() {
        return true;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

