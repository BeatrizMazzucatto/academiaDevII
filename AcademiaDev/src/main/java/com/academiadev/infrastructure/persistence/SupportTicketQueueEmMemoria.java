package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class SupportTicketQueueEmMemoria implements SupportTicketQueue {
    private final Queue<SupportTicket> tickets;
    
    public SupportTicketQueueEmMemoria() {
        this.tickets = new ArrayDeque<>();
    }
    
    @Override
    public void addTicket(SupportTicket ticket) {
        tickets.offer(ticket);
    }
    
    @Override
    public Optional<SupportTicket> nextTicket() {
        return Optional.ofNullable(tickets.poll());
    }
    
    @Override
    public boolean isEmpty() {
        return tickets.isEmpty();
    }
    
    @Override
    public int size() {
        return tickets.size();
    }
}

