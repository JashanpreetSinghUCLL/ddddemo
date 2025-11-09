package be.ucll.craftmanship.DDDDemo.library.application.services;

import be.ucll.craftmanship.DDDDemo.library.application.dto.BookResponse;
import be.ucll.craftmanship.DDDDemo.library.application.dto.CreateBookRequest;
import be.ucll.craftmanship.DDDDemo.library.domain.entities.Book;
import be.ucll.craftmanship.DDDDemo.library.domain.repositories.BookRepository;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.ISBN;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * APPLICATION SERVICE: BookService
 * 
 * Responsibilities:
 * - Coordinates domain objects
 * - Handles infrastructure concerns (transactions, etc.)
 * - Does NOT contain business logic (that's in the domain)
 * - Translates between DTOs and domain objects
 */
@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Creates a new book in the system
     */
    public BookResponse createBook(CreateBookRequest request) {
        ISBN isbn = new ISBN(request.isbn());
        
        if (bookRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " already exists");
        }
        
        Book book = new Book(
            BookId.generate(),
            isbn,
            request.title(),
            request.author()
        );
        
        Book savedBook = bookRepository.save(book);
        return BookResponse.from(savedBook);
    }
    
    /**
     * Gets all books
     */
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
            .stream()
            .map(BookResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all available books
     */
    public List<BookResponse> getAvailableBooks() {
        return bookRepository.findByAvailableTrue()
            .stream()
            .map(BookResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets a book by ID
     */
    public BookResponse getBookById(String bookId) {
        Book book = bookRepository.findById(BookId.from(bookId))
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        return BookResponse.from(book);
    }
    
    /**
     * Searches books by title
     */
    public List<BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
            .stream()
            .map(BookResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Searches books by author
     */
    public List<BookResponse> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
            .stream()
            .map(BookResponse::from)
            .collect(Collectors.toList());
    }
}

