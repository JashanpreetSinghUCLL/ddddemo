package be.ucll.craftmanship.DDDDemo.library.domain.aggregates;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * AGGREGATE ROOT: Loan
 * 
 * KEY DDD CONCEPTS DEMONSTRATED:
 * 
 * 1. AGGREGATE ROOT: This is the ONLY entry point to the loan aggregate
 * 2. REFERENCE BY ID: References Book and Member by ID only (not full objects)
 * 3. BUSINESS RULES: All loan rules are enforced here
 * 4. CONSISTENCY BOUNDARY: All changes are atomic within this aggregate
 * 5. NO DIRECT ENTITY ACCESS: External code can't bypass this root
 * 
 * Business Rules Enforced:
 * - Loan period is maximum 30 days
 * - Can't return a loan that's already returned
 * - Can't extend a returned loan
 * - Late fees are calculated based on overdue days
 */
@Entity
@Table(name = "loans")
public class Loan {
    
    @EmbeddedId
    private LoanId id;
    
    // ✅ CORRECT: Reference other aggregates by ID only
    @Column(nullable = false)
    private BookId bookId;
    
    @Column(nullable = false)
    private MemberId memberId;
    
    @Column(nullable = false)
    private LocalDate borrowedDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @Column
    private LocalDate returnedDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;
    
    @Column
    private String notes;
    
    // JPA requires default constructor
    protected Loan() {}
    
    /**
     * Creates a new loan
     * Business Rule: Loan period is 30 days by default
     */
    public Loan(LoanId id, BookId bookId, MemberId memberId) {
        if (id == null) {
            throw new IllegalArgumentException("LoanId cannot be null");
        }
        if (bookId == null) {
            throw new IllegalArgumentException("BookId cannot be null");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("MemberId cannot be null");
        }
        
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedDate = LocalDate.now();
        this.dueDate = borrowedDate.plusDays(30); // Business Rule: 30 days loan period
        this.status = LoanStatus.ACTIVE;
        this.returnedDate = null;
    }
    
    /**
     * Returns the book
     * Business Rule: Can't return an already returned loan
     */
    public void returnBook() {
        if (status == LoanStatus.RETURNED) {
            throw new IllegalStateException("Loan is already returned");
        }
        
        this.returnedDate = LocalDate.now();
        this.status = LoanStatus.RETURNED;
    }
    
    /**
     * Extends the loan period
     * Business Rules:
     * - Can't extend a returned loan
     * - Can only extend by max 14 days
     * - Can't extend more than once
     */
    public void extendLoan(int days) {
        if (status == LoanStatus.RETURNED) {
            throw new IllegalStateException("Cannot extend a returned loan");
        }
        
        if (status == LoanStatus.EXTENDED) {
            throw new IllegalStateException("Loan has already been extended once");
        }
        
        if (days <= 0 || days > 14) {
            throw new IllegalArgumentException("Extension must be between 1 and 14 days");
        }
        
        this.dueDate = this.dueDate.plusDays(days);
        this.status = LoanStatus.EXTENDED;
    }
    
    /**
     * Checks if the loan is overdue
     */
    public boolean isOverdue() {
        if (status == LoanStatus.RETURNED) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * Calculates the number of days overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    /**
     * Adds a note to the loan
     */
    public void addNote(String note) {
        if (note == null || note.isBlank()) {
            throw new IllegalArgumentException("Note cannot be null or empty");
        }
        
        if (this.notes == null) {
            this.notes = note;
        } else {
            this.notes += "\n" + note;
        }
    }
    
    // Getters
    public LoanId getId() {
        return id;
    }
    
    public BookId getBookId() {
        return bookId;
    }
    
    public MemberId getMemberId() {
        return memberId;
    }
    
    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public LocalDate getReturnedDate() {
        return returnedDate;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan loan)) return false;
        return id != null && id.equals(loan.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return String.format("Loan[id=%s, bookId=%s, memberId=%s, status=%s, dueDate=%s]",
            id, bookId, memberId, status, dueDate);
    }
}

