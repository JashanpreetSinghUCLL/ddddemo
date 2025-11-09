package be.ucll.craftmanship.DDDDemo.library.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for borrowing a book
 */
public record BorrowBookRequest(
    @NotBlank(message = "Book ID is required")
    String bookId,
    
    @NotBlank(message = "Member ID is required")
    String memberId
) {}

