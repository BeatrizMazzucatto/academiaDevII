package com.academiadev.domain.entities;

public class PremiumPlan extends SubscriptionPlan {
    
    public PremiumPlan() {
        super("PremiumPlan", -1);
    }
    
    @Override
    public boolean canEnroll(int currentActiveEnrollments) {
        return true;
    }
}

