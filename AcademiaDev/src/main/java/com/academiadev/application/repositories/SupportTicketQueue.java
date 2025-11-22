package com.academiadev.application.repositories;

import com.academiadev.domain.entities.SupportTicket;

import java.util.Optional;

public interface SupportTicketQueue {
    void addTicket(SupportTicket ticket);
    Optional<SupportTicket> nextTicket();
    boolean isEmpty();
    int size();
}

