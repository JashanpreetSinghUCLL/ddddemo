package be.ucll.craftmanship.DDDDemo.library.domain.events;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DOMAIN EVENT: LoanExtendedEvent
 * 
 * Published when a loan period is extended
 */
public record LoanExtendedEvent(
    UUID eventId,
    LocalDateTime occurredOn,
    LoanId loanId,
    MemberId memberId,
    LocalDate newDueDate,
    int daysExtended
) implements DomainEvent {
    
    public LoanExtendedEvent(
        LoanId loanId,
        MemberId memberId,
        LocalDate newDueDate,
        int daysExtended
    ) {
        this(
            UUID.randomUUID(),
            LocalDateTime.now(),
            loanId,
            memberId,
            newDueDate,
            daysExtended
        );
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return "LoanExtended";
    }
}

