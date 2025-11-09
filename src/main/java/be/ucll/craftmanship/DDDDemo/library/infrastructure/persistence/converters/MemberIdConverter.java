package be.ucll.craftmanship.DDDDemo.library.infrastructure.persistence.converters;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA Converter for MemberId value object
 */
@Converter(autoApply = true)
public class MemberIdConverter implements AttributeConverter<MemberId, UUID> {
    
    @Override
    public UUID convertToDatabaseColumn(MemberId memberId) {
        return memberId == null ? null : memberId.value();
    }
    
    @Override
    public MemberId convertToEntityAttribute(UUID uuid) {
        return uuid == null ? null : new MemberId(uuid);
    }
}

