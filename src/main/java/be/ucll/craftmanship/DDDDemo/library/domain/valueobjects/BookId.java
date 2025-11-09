package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

import java.util.UUID;

/**
 * VALUE OBJECT: BookId
 * - Wrapped ID for type safety
 * - Prevents mixing up IDs
 * - Immutable
 */
public record BookId(UUID value) {
    
    public BookId {
        if (value == null) {
            throw new IllegalArgumentException("BookId cannot be null");
        }
    }
    
    public static BookId generate() {
        return new BookId(UUID.randomUUID());
    }
    
    public static BookId from(String uuid) {
        return new BookId(UUID.fromString(uuid));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

