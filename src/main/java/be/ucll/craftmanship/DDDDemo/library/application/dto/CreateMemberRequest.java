package be.ucll.craftmanship.DDDDemo.library.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating a new member
 */
public record CreateMemberRequest(
    @NotBlank(message = "Name is required")
    String name,
    
    @NotBlank(message = "Email is required")
    String email,
    
    @NotBlank(message = "Street is required")
    String street,
    
    @NotBlank(message = "City is required")
    String city,
    
    @NotBlank(message = "Postal code is required")
    String postalCode,
    
    @NotBlank(message = "Country is required")
    String country
) {}

