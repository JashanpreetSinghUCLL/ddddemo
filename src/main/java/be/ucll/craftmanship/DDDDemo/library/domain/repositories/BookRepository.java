package be.ucll.craftmanship.DDDDemo.library.domain.repositories;

import be.ucll.craftmanship.DDDDemo.library.domain.entities.Book;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.ISBN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Book Entity
 * 
 * Note: In this simple example, Book is not an aggregate root with child entities,
 * but in a more complex system, it could be part of a "Catalog" aggregate with
 * BookCopies, Reviews, etc.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, BookId> {
    
    /**
     * Finds a book by ISBN
     * ISBNs are unique per book
     */
    Optional<Book> findByIsbn(ISBN isbn);
    
    /**
     * Finds all available books
     */
    List<Book> findByAvailableTrue();
    
    /**
     * Searches books by title (case-insensitive, partial match)
     */
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Searches books by author (case-insensitive, partial match)
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    /**
     * Checks if a book with given ISBN exists
     */
    boolean existsByIsbn(ISBN isbn);
}

