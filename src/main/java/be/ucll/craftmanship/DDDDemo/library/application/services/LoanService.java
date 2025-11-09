package be.ucll.craftmanship.DDDDemo.library.application.services;

import be.ucll.craftmanship.DDDDemo.library.application.dto.BorrowBookRequest;
import be.ucll.craftmanship.DDDDemo.library.application.dto.LoanResponse;
import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.Loan;
import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.LoanStatus;
import be.ucll.craftmanship.DDDDemo.library.domain.entities.Book;
import be.ucll.craftmanship.DDDDemo.library.domain.entities.Member;
import be.ucll.craftmanship.DDDDemo.library.domain.events.BookBorrowedEvent;
import be.ucll.craftmanship.DDDDemo.library.domain.events.BookReturnedEvent;
import be.ucll.craftmanship.DDDDemo.library.domain.events.LoanExtendedEvent;
import be.ucll.craftmanship.DDDDemo.library.domain.repositories.BookRepository;
import be.ucll.craftmanship.DDDDemo.library.domain.repositories.LoanRepository;
import be.ucll.craftmanship.DDDDemo.library.domain.repositories.MemberRepository;
import be.ucll.craftmanship.DDDDemo.library.domain.services.LateFeeCalculationService;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * APPLICATION SERVICE: LoanService
 * 
 * This is where the magic happens! This service:
 * 1. Coordinates multiple aggregates (Loan, Book, Member)
 * 2. Enforces cross-aggregate business rules
 * 3. Publishes domain events
 * 4. Uses domain services (LateFeeCalculationService)
 * 
 * KEY DDD PATTERN:
 * - Application service coordinates aggregates
 * - Domain logic stays in domain objects
 * - Events enable loose coupling
 */
@Service
@Transactional
public class LoanService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private static final int MAX_ACTIVE_LOANS_PER_MEMBER = 5;
    
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LateFeeCalculationService lateFeeService;
    
    public LoanService(
        LoanRepository loanRepository,
        BookRepository bookRepository,
        MemberRepository memberRepository,
        LateFeeCalculationService lateFeeService
    ) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.lateFeeService = lateFeeService;
    }
    
    /**
     * Borrows a book
     * 
     * Business Rules:
     * - Book must be available
     * - Member must be active
     * - Member can't have more than 5 active loans
     */
    public LoanResponse borrowBook(BorrowBookRequest request) {
        BookId bookId = BookId.from(request.bookId());
        MemberId memberId = MemberId.from(request.memberId());
        
        // Load aggregates
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        
        // Validate business rules
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available: " + book.getTitle());
        }
        
        if (!member.isActive()) {
            throw new IllegalStateException("Member is not active: " + member.getName());
        }
        
        long activeLoans = loanRepository.countByMemberIdAndStatus(memberId, LoanStatus.ACTIVE);
        if (activeLoans >= MAX_ACTIVE_LOANS_PER_MEMBER) {
            throw new IllegalStateException(
                "Member has reached maximum active loans limit: " + MAX_ACTIVE_LOANS_PER_MEMBER
            );
        }
        
        // Create loan aggregate
        Loan loan = new Loan(LoanId.generate(), bookId, memberId);
        
        // Mark book as unavailable
        book.markAsUnavailable();
        
        // Save changes
        loanRepository.save(loan);
        bookRepository.save(book);
        
        // Publish domain event
        publishEvent(new BookBorrowedEvent(
            loan.getId(),
            bookId,
            memberId,
            loan.getDueDate()
        ));
        
        logger.info("Book borrowed: {} by member: {}", bookId, memberId);
        
        return LoanResponse.from(loan);
    }
    
    /**
     * Returns a book
     */
    public LoanResponse returnBook(String loanId) {
        LoanId id = LoanId.from(loanId);
        
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
        
        BookId bookId = loan.getBookId();
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        
        boolean wasOverdue = loan.isOverdue();
        
        // Return the book (domain logic)
        loan.returnBook();
        
        // Mark book as available
        book.markAsAvailable();
        
        // Save changes
        loanRepository.save(loan);
        bookRepository.save(book);
        
        // Publish domain event
        publishEvent(new BookReturnedEvent(
            loan.getId(),
            bookId,
            loan.getMemberId(),
            loan.getReturnedDate(),
            wasOverdue
        ));
        
        logger.info("Book returned: {} by member: {}", bookId, loan.getMemberId());
        
        return LoanResponse.from(loan);
    }
    
    /**
     * Extends a loan
     */
    public LoanResponse extendLoan(String loanId, int days) {
        LoanId id = LoanId.from(loanId);
        
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
        
        // Domain logic handles validation
        loan.extendLoan(days);
        
        loanRepository.save(loan);
        
        // Publish domain event
        publishEvent(new LoanExtendedEvent(
            loan.getId(),
            loan.getMemberId(),
            loan.getDueDate(),
            days
        ));
        
        logger.info("Loan extended: {} by {} days", loanId, days);
        
        return LoanResponse.from(loan);
    }
    
    /**
     * Gets all loans for a member
     */
    public List<LoanResponse> getLoansByMember(String memberId) {
        MemberId id = MemberId.from(memberId);
        return loanRepository.findByMemberIdAndStatus(id, LoanStatus.ACTIVE)
            .stream()
            .map(LoanResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all overdue loans
     */
    public List<LoanResponse> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now())
            .stream()
            .map(LoanResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Calculates late fee for a loan
     * Uses the LateFeeCalculationService (Domain Service)
     */
    public String calculateLateFee(String loanId) {
        LoanId id = LoanId.from(loanId);
        
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
        
        if (!loan.isOverdue()) {
            return "No late fee - loan is not overdue";
        }
        
        // Use domain service to calculate fee
        Money lateFee = lateFeeService.calculateLateFee(loan);
        
        return String.format("Late fee for loan %s: %s (%d days overdue)",
            loanId, lateFee, loan.getDaysOverdue());
    }
    
    /**
     * Publishes a domain event
     * In a real system, this would use an event bus or message broker
     * For simplicity, we just log it
     */
    private void publishEvent(Object event) {
        logger.info("Domain Event Published: {}", event);
        // In a real system:
        // eventPublisher.publish(event);
        // Or: applicationEventPublisher.publishEvent(event);
    }
}

