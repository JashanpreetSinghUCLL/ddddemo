package be.ucll.craftmanship.DDDDemo.library.domain.events;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DOMAIN EVENT: BookReturnedEvent
 * 
 * Published when a book is returned
 * 
 * USE CASES:
 * - BookAggregate subscribes to mark book as available
 * - LateFeeService subscribes to calculate any late fees
 * - NotificationService subscribes to send return confirmation
 * - AuditService subscribes to log the return
 */
public record BookReturnedEvent(
    UUID eventId,
    LocalDateTime occurredOn,
    LoanId loanId,
    BookId bookId,
    MemberId memberId,
    LocalDate returnedDate,
    boolean wasOverdue
) implements DomainEvent {
    
    public BookReturnedEvent(
        LoanId loanId, 
        BookId bookId, 
        MemberId memberId, 
        LocalDate returnedDate,
        boolean wasOverdue
    ) {
        this(
            UUID.randomUUID(),
            LocalDateTime.now(),
            loanId,
            bookId,
            memberId,
            returnedDate,
            wasOverdue
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
        return "BookReturned";
    }
    
    @Override
    public String toString() {
        return String.format("BookReturnedEvent[loanId=%s, bookId=%s, memberId=%s, returnedDate=%s, wasOverdue=%s]",
            loanId, bookId, memberId, returnedDate, wasOverdue);
    }
}

