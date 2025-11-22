package com.academiadev.domain.entities;

public abstract class SubscriptionPlan {
    protected String name;
    protected int maxActiveEnrollments;
    
    public SubscriptionPlan(String name, int maxActiveEnrollments) {
        this.name = name;
        this.maxActiveEnrollments = maxActiveEnrollments;
    }
    
    public String getName() {
        return name;
    }
    
    public int getMaxActiveEnrollments() {
        return maxActiveEnrollments;
    }
    
    public boolean canEnroll(int currentActiveEnrollments) {
        if (maxActiveEnrollments == -1) {
            return true;
        }
        return currentActiveEnrollments < maxActiveEnrollments;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

