package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

import java.util.UUID;

/**
 * VALUE OBJECT: MemberId
 * - Wrapped ID for type safety
 * - Prevents mixing up different types of IDs (BookId, MemberId, etc.)
 * - Immutable
 * 
 * Example of TYPE SAFETY:
 * Without this: addBook(UUID id) - could pass wrong ID type
 * With this: addBook(BookId id) - compiler catches mistakes!
 */
public record MemberId(UUID value) {
    
    public MemberId {
        if (value == null) {
            throw new IllegalArgumentException("MemberId cannot be null");
        }
    }
    
    public static MemberId generate() {
        return new MemberId(UUID.randomUUID());
    }
    
    public static MemberId from(String uuid) {
        return new MemberId(UUID.fromString(uuid));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

