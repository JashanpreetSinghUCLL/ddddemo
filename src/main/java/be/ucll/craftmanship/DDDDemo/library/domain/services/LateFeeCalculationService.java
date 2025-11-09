package be.ucll.craftmanship.DDDDemo.library.domain.services;

import be.ucll.craftmanship.DDDDemo.library.domain.aggregates.Loan;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Money;
import org.springframework.stereotype.Service;

/**
 * DOMAIN SERVICE: LateFeeCalculationService
 * 
 * WHY A DOMAIN SERVICE?
 * - Stateless operation
 * - Business logic that doesn't naturally belong to Loan, Book, or Member
 * - Calculation involves business rules that might depend on:
 *   - Number of days overdue
 *   - Book type (e.g., rare books have higher fees)
 *   - Member type (e.g., students get discounted fees)
 *   - Library policies
 * 
 * This prevents "anemic domain model" by keeping business logic in domain layer,
 * not in application services.
 * 
 * Business Rules:
 * - First 7 days: €0.50 per day
 * - 8-14 days: €1.00 per day
 * - 15+ days: €2.00 per day
 */
@Service
public class LateFeeCalculationService {
    
    private static final Money TIER_1_FEE = Money.euro(0.50); // First 7 days
    private static final Money TIER_2_FEE = Money.euro(1.00); // Days 8-14
    private static final Money TIER_3_FEE = Money.euro(2.00); // Days 15+
    
    /**
     * Calculates late fee for an overdue loan
     * 
     * @param loan The overdue loan
     * @return The calculated late fee
     * @throws IllegalArgumentException if loan is not overdue
     */
    public Money calculateLateFee(Loan loan) {
        if (!loan.isOverdue()) {
            throw new IllegalArgumentException("Cannot calculate late fee for non-overdue loan");
        }
        
        long daysOverdue = loan.getDaysOverdue();
        return calculateFeeByDays(daysOverdue);
    }
    
    /**
     * Calculates fee based on number of days overdue
     * 
     * Business Logic:
     * - Days 1-7: €0.50/day
     * - Days 8-14: €1.00/day
     * - Days 15+: €2.00/day
     */
    private Money calculateFeeByDays(long daysOverdue) {
        Money totalFee = Money.euro(0);
        
        // Tier 1: First 7 days at €0.50/day
        if (daysOverdue > 0) {
            long tier1Days = Math.min(daysOverdue, 7);
            totalFee = totalFee.add(TIER_1_FEE.multiply((int) tier1Days));
        }
        
        // Tier 2: Days 8-14 at €1.00/day
        if (daysOverdue > 7) {
            long tier2Days = Math.min(daysOverdue - 7, 7);
            totalFee = totalFee.add(TIER_2_FEE.multiply((int) tier2Days));
        }
        
        // Tier 3: Days 15+ at €2.00/day
        if (daysOverdue > 14) {
            long tier3Days = daysOverdue - 14;
            totalFee = totalFee.add(TIER_3_FEE.multiply((int) tier3Days));
        }
        
        return totalFee;
    }
    
    /**
     * Estimates future late fee if book is not returned by a certain number of days
     * Useful for showing warnings to members
     */
    public Money estimateFutureLateFee(int projectedDaysLate) {
        if (projectedDaysLate <= 0) {
            return Money.euro(0);
        }
        return calculateFeeByDays(projectedDaysLate);
    }
    
    /**
     * Calculates daily late fee rate based on how many days overdue
     */
    public Money getCurrentDailyRate(long daysOverdue) {
        if (daysOverdue <= 0) {
            return Money.euro(0);
        } else if (daysOverdue <= 7) {
            return TIER_1_FEE;
        } else if (daysOverdue <= 14) {
            return TIER_2_FEE;
        } else {
            return TIER_3_FEE;
        }
    }
}

