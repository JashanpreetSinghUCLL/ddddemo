package be.ucll.craftmanship.DDDDemo.library.domain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * VALUE OBJECT: Money
 * - Immutable
 * - Encapsulates currency logic
 * - Prevents precision errors with BigDecimal
 */
public record Money(BigDecimal amount, String currency) {
    
    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        
        // Ensure 2 decimal places
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }
    
    public static Money euro(double amount) {
        return new Money(BigDecimal.valueOf(amount), "EUR");
    }
    
    @Override
    public String toString() {
        return String.format("%s %.2f", currency, amount);
    }
}

