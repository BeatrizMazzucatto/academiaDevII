package com.academiadev.application.usecases;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;
import com.academiadev.domain.entities.User;

public class AbrirTicketUseCase {
    private final SupportTicketQueue ticketQueue;
    
    public AbrirTicketUseCase(SupportTicketQueue ticketQueue) {
        this.ticketQueue = ticketQueue;
    }
    
    public SupportTicket execute(String title, String message, User user) {
        SupportTicket ticket = new SupportTicket(title, message, user);
        ticketQueue.addTicket(ticket);
        return ticket;
    }
}

