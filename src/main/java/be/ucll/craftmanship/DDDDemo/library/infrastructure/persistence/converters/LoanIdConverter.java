package be.ucll.craftmanship.DDDDemo.library.infrastructure.persistence.converters;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.LoanId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA Converter for LoanId value object
 */
@Converter(autoApply = true)
public class LoanIdConverter implements AttributeConverter<LoanId, UUID> {
    
    @Override
    public UUID convertToDatabaseColumn(LoanId loanId) {
        return loanId == null ? null : loanId.value();
    }
    
    @Override
    public LoanId convertToEntityAttribute(UUID uuid) {
        return uuid == null ? null : new LoanId(uuid);
    }
}

