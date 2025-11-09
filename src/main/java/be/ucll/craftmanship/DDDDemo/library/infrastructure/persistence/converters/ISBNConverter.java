package be.ucll.craftmanship.DDDDemo.library.infrastructure.persistence.converters;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.ISBN;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for ISBN value object
 */
@Converter(autoApply = true)
public class ISBNConverter implements AttributeConverter<ISBN, String> {
    
    @Override
    public String convertToDatabaseColumn(ISBN isbn) {
        return isbn == null ? null : isbn.value();
    }
    
    @Override
    public ISBN convertToEntityAttribute(String value) {
        return value == null ? null : new ISBN(value);
    }
}

