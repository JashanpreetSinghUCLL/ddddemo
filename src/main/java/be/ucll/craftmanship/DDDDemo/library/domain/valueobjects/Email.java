package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

/**
 * VALUE OBJECT: Email
 * - Immutable
 * - Validates email format
 * - No identity tracking needed
 */
public record Email(String value) {
    
    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }
    
    @Override
    public String toString() {
        return value;
    }
}

