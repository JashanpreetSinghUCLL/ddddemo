package be.ucll.craftmanship.DDDDemo.library.application.dto;

import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.Loan;

/**
 * Data Transfer Object for Loan responses
 */
public record LoanResponse(
    String id,
    String bookId,
    String memberId,
    String borrowedDate,
    String dueDate,
    String returnedDate,
    String status,
    boolean overdue,
    long daysOverdue
) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
            loan.getId().toString(),
            loan.getBookId().toString(),
            loan.getMemberId().toString(),
            loan.getBorrowedDate().toString(),
            loan.getDueDate().toString(),
            loan.getReturnedDate() != null ? loan.getReturnedDate().toString() : null,
            loan.getStatus().toString(),
            loan.isOverdue(),
            loan.getDaysOverdue()
        );
    }
}

