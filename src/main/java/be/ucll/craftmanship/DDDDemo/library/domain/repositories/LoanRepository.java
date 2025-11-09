package be.ucll.craftmanship.DDDDemo.library.domain.repositories;

import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.Loan;
import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.LoanStatus;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DDD REPOSITORY: LoanRepository
 * 
 * KEY POINTS:
 * - Operates on the Loan AGGREGATE (not individual entities within it)
 * - Repository type matches Aggregate Root type
 * - Provides abstraction over persistence
 * - Hides database implementation details from domain
 * 
 * IMPORTANT: In DDD, you save/load ENTIRE aggregates, not parts of them
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, LoanId> {
    
    /**
     * Finds all active loans for a specific member
     * Useful for checking if member has outstanding loans
     */
    List<Loan> findByMemberIdAndStatus(MemberId memberId, LoanStatus status);
    
    /**
     * Finds all loans for a specific book
     * Useful for checking book's loan history
     */
    List<Loan> findByBookId(BookId bookId);
    
    /**
     * Finds all overdue loans (due date passed, not yet returned)
     * Useful for generating overdue reports
     */
    @Query("SELECT l FROM Loan l WHERE l.dueDate < :today AND l.status != 'RETURNED'")
    List<Loan> findOverdueLoans(LocalDate today);
    
    /**
     * Finds active loan for a specific book
     * Returns empty if book is available
     */
    Optional<Loan> findByBookIdAndStatus(BookId bookId, LoanStatus status);
    
    /**
     * Counts active loans for a member
     * Useful for enforcing borrowing limits
     */
    long countByMemberIdAndStatus(MemberId memberId, LoanStatus status);
    
    /**
     * Finds all loans due within a certain number of days
     * Useful for sending reminder notifications
     */
    @Query("SELECT l FROM Loan l WHERE l.dueDate BETWEEN :startDate AND :endDate AND l.status != 'RETURNED'")
    List<Loan> findLoansDueBetween(LocalDate startDate, LocalDate endDate);
}

