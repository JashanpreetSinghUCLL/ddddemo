package be.ucll.craftmanship.DDDDemo.library.infrastructure.persistence.converters;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA Converter for BookId value object
 * Converts between BookId and UUID for database storage
 */
@Converter(autoApply = true)
public class BookIdConverter implements AttributeConverter<BookId, UUID> {
    
    @Override
    public UUID convertToDatabaseColumn(BookId bookId) {
        return bookId == null ? null : bookId.value();
    }
    
    @Override
    public BookId convertToEntityAttribute(UUID uuid) {
        return uuid == null ? null : new BookId(uuid);
    }
}

