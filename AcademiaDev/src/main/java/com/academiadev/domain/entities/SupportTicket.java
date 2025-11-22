package com.academiadev.domain.entities;

public class SupportTicket {
    private String title;
    private String message;
    private User user;
    
    public SupportTicket(String title, String message, User user) {
        this.title = title;
        this.message = message;
        this.user = user;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "SupportTicket{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", user=" + user.getEmail() +
                '}';
    }
}

