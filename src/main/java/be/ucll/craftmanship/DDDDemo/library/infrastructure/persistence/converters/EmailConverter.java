package be.ucll.craftmanship.DDDDemo.library.infrastructure.persistence.converters;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for Email value object
 */
@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {
    
    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.value();
    }
    
    @Override
    public Email convertToEntityAttribute(String value) {
        return value == null ? null : new Email(value);
    }
}

