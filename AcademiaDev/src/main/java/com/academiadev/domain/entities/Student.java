package com.academiadev.domain.entities;

public class Student extends User {
    private SubscriptionPlan subscriptionPlan;
    
    public Student(String name, String email, SubscriptionPlan subscriptionPlan) {
        super(name, email);
        this.subscriptionPlan = subscriptionPlan;
    }
    
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }
    
    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }
    
    @Override
    public boolean isAdmin() {
        return false;
    }
    
    public boolean canEnroll(int currentActiveEnrollments) {
        return subscriptionPlan.canEnroll(currentActiveEnrollments);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", subscriptionPlan=" + subscriptionPlan +
                '}';
    }
}

