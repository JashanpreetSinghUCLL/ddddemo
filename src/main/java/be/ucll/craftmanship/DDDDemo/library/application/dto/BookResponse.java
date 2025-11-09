package be.ucll.craftmanship.DDDDemo.library.application.dto;

import be.ucll.craftmanship.DDDDemo.library.domain.entities.Book;

/**
 * Data Transfer Object for Book responses
 */
public record BookResponse(
    String id,
    String isbn,
    String title,
    String author,
    boolean available
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
            book.getId().toString(),
            book.getIsbn().toString(),
            book.getTitle(),
            book.getAuthor(),
            book.isAvailable()
        );
    }
}

