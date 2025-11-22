package com.academiadev.application.usecases;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;
import com.academiadev.domain.exceptions.BusinessException;

import java.util.Optional;

public class AtenderTicketUseCase {
    private final SupportTicketQueue ticketQueue;
    
    public AtenderTicketUseCase(SupportTicketQueue ticketQueue) {
        this.ticketQueue = ticketQueue;
    }
    
    public SupportTicket execute() {
        Optional<SupportTicket> ticket = ticketQueue.nextTicket();
        if (ticket.isEmpty()) {
            throw new BusinessException("Não há tickets na fila para atender");
        }
        return ticket.get();
    }
}

