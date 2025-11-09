package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

/**
 * VALUE OBJECT: Address
 * - Immutable
 * - No identity - if two addresses have same values, they're equal
 * - Cannot be modified - must create new Address if changes needed
 */
public record Address(
    String street,
    String city,
    String postalCode,
    String country
) {
    
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be null or empty");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }
    
    public String getFullAddress() {
        return String.format("%s, %s %s, %s", street, postalCode, city, country);
    }
}

