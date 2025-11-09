package be.ucll.craftmanship.DDDDemo.library.domain.events;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DOMAIN EVENT: LoanOverdueEvent
 * 
 * Published when a loan becomes overdue
 * 
 * USE CASES:
 * - NotificationService subscribes to send overdue reminders
 * - LateFeeService subscribes to start calculating daily late fees
 * - MemberService subscribes to update member's overdue loan count
 * - ReportingService subscribes to track overdue metrics
 */
public record LoanOverdueEvent(
    UUID eventId,
    LocalDateTime occurredOn,
    LoanId loanId,
    BookId bookId,
    MemberId memberId,
    LocalDate dueDate,
    long daysOverdue
) implements DomainEvent {
    
    public LoanOverdueEvent(
        LoanId loanId,
        BookId bookId,
        MemberId memberId,
        LocalDate dueDate,
        long daysOverdue
    ) {
        this(
            UUID.randomUUID(),
            LocalDateTime.now(),
            loanId,
            bookId,
            memberId,
            dueDate,
            daysOverdue
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
        return "LoanOverdue";
    }
    
    @Override
    public String toString() {
        return String.format("LoanOverdueEvent[loanId=%s, bookId=%s, memberId=%s, daysOverdue=%d]",
            loanId, bookId, memberId, daysOverdue);
    }
}

