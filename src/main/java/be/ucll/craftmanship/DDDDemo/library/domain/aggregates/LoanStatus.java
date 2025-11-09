package be.ucll.craftmanship.DDDDemo.library.domain.aggregates;

/**
 * Enum representing the status of a loan
 */
public enum LoanStatus {
    ACTIVE,      // Currently borrowed
    EXTENDED,    // Loan period has been extended
    RETURNED,    // Book has been returned
    OVERDUE      // Past due date (can be computed, but storing for query efficiency)
}

