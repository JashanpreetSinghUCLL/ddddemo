package be.ucll.craftmanship.DDDDemo.library.domain.entities;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.BookId;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.ISBN;
import jakarta.persistence.*;

/**
 * ENTITY: Book
 * - Has unique identity (BookId)
 * - Mutable - properties can change over time
 * - Same BookId = same book, even if title changes
 * - Can be tracked across time
 */
@Entity
@Table(name = "books")
public class Book {
    
    @EmbeddedId
    private BookId id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "isbn", unique = true))
    private ISBN isbn;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
    private boolean available;
    
    // JPA requires default constructor
    protected Book() {}
    
    public Book(BookId id, ISBN isbn, String title, String author) {
        if (id == null) {
            throw new IllegalArgumentException("BookId cannot be null");
        }
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true; // New books are available by default
    }
    
    // Business methods
    public void markAsUnavailable() {
        if (!available) {
            throw new IllegalStateException("Book is already unavailable");
        }
        this.available = false;
    }
    
    public void markAsAvailable() {
        if (available) {
            throw new IllegalStateException("Book is already available");
        }
        this.available = true;
    }
    
    public void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = newTitle;
    }
    
    // Getters
    public BookId getId() {
        return id;
    }
    
    public ISBN getIsbn() {
        return isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        // Entities are equal if they have the same ID
        return id != null && id.equals(book.id);
    }
    
    @Override
    public int hashCode() {
        // Use ID for hash code
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return String.format("Book[id=%s, title=%s, author=%s, available=%s]", 
            id, title, author, available);
    }
}

