package be.ucll.craftmanship.DDDDemo.library.api.controllers;

import be.ucll.craftmanship.DDDDemo.library.application.dto.BorrowBookRequest;
import be.ucll.craftmanship.DDDDemo.library.application.dto.LoanResponse;
import be.ucll.craftmanship.DDDDemo.library.application.services.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Loan operations
 * This is where users interact with the loan aggregate
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    private final LoanService loanService;
    
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    
    @PostMapping("/borrow")
    public ResponseEntity<LoanResponse> borrowBook(@Valid @RequestBody BorrowBookRequest request) {
        LoanResponse response = loanService.borrowBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/{loanId}/return")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable String loanId) {
        LoanResponse response = loanService.returnBook(loanId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{loanId}/extend")
    public ResponseEntity<LoanResponse> extendLoan(
        @PathVariable String loanId,
        @RequestParam int days
    ) {
        LoanResponse response = loanService.extendLoan(loanId, days);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanResponse>> getLoansByMember(@PathVariable String memberId) {
        List<LoanResponse> loans = loanService.getLoansByMember(memberId);
        return ResponseEntity.ok(loans);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<LoanResponse>> getOverdueLoans() {
        List<LoanResponse> loans = loanService.getOverdueLoans();
        return ResponseEntity.ok(loans);
    }
    
    @GetMapping("/{loanId}/late-fee")
    public ResponseEntity<String> calculateLateFee(@PathVariable String loanId) {
        String lateFeeInfo = loanService.calculateLateFee(loanId);
        return ResponseEntity.ok(lateFeeInfo);
    }
}

