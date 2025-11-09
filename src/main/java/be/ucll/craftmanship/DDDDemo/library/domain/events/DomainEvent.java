package be.ucll.craftmanship.DDDDemo.library.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events
 * 
 * KEY CHARACTERISTICS:
 * - Immutable (represent past events)
 * - Named in past tense (e.g., "BookBorrowed", not "BorrowBook")
 * - Include timestamp of when event occurred
 * - Have unique event ID for tracing
 */
public interface DomainEvent {
    
    UUID getEventId();
    
    LocalDateTime getOccurredOn();
    
    String getEventType();
}

