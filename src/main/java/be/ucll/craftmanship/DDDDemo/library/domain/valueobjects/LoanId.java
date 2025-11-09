package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

import java.util.UUID;

/**
 * VALUE OBJECT: LoanId
 * - Wrapped ID for type safety
 * - Used to identify Loan aggregate
 * - Immutable
 */
public record LoanId(UUID value) {
    
    public LoanId {
        if (value == null) {
            throw new IllegalArgumentException("LoanId cannot be null");
        }
    }
    
    public static LoanId generate() {
        return new LoanId(UUID.randomUUID());
    }
    
    public static LoanId from(String uuid) {
        return new LoanId(UUID.fromString(uuid));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

