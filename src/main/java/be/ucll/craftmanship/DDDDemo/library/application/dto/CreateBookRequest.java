package be.ucll.craftmanship.DDDDemo.library.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating a new book
 * DTOs live in the application layer, not the domain layer
 */
public record CreateBookRequest(
    @NotBlank(message = "ISBN is required")
    String isbn,
    
    @NotBlank(message = "Title is required")
    String title,
    
    @NotBlank(message = "Author is required")
    String author
) {}

