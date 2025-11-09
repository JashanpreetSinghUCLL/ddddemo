package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

/**
 * VALUE OBJECT: ISBN
 * - Immutable (Java Record)
 * - No unique ID
 * - Two ISBNs with same value are equal
 * - Validates format on creation
 */
public record ISBN(String value) {
    
    public ISBN {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        // Remove hyphens for validation
        String cleanedISBN = value.replace("-", "").replace(" ", "");
        
        if (cleanedISBN.length() != 10 && cleanedISBN.length() != 13) {
            throw new IllegalArgumentException(
                "ISBN must be either 10 or 13 characters long (excluding hyphens)"
            );
        }
        
        if (!cleanedISBN.matches("\\d+")) {
            throw new IllegalArgumentException("ISBN must contain only digits");
        }
    }
    
    @Override
    public String toString() {
        return value;
    }
}

