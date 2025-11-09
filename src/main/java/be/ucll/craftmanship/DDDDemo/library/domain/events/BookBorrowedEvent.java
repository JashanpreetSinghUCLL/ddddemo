package be.ucll.craftmanship.DDDDemo.library.domain.events;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DOMAIN EVENT: BookBorrowedEvent
 * 
 * Published when a book is borrowed by a member
 * 
 * USE CASES:
 * - InventoryAggregate subscribes to update book availability
 * - NotificationService subscribes to send confirmation email
 * - AuditService subscribes to log the action
 * - AnalyticsService subscribes to track borrowing trends
 */
public record BookBorrowedEvent(
    UUID eventId,
    LocalDateTime occurredOn,
    LoanId loanId,
    BookId bookId,
    MemberId memberId,
    LocalDate dueDate
) implements DomainEvent {
    
    public BookBorrowedEvent(LoanId loanId, BookId bookId, MemberId memberId, LocalDate dueDate) {
        this(
            UUID.randomUUID(),
            LocalDateTime.now(),
            loanId,
            bookId,
            memberId,
            dueDate
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
        return "BookBorrowed";
    }
    
    @Override
    public String toString() {
        return String.format("BookBorrowedEvent[loanId=%s, bookId=%s, memberId=%s, dueDate=%s, occurredOn=%s]",
            loanId, bookId, memberId, dueDate, occurredOn);
    }
}

