package be.ucll.craftmanship.DDDDemo.library.domain.entities;

import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Address;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Email;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * ENTITY: Member
 * - Has unique identity (MemberId)
 * - Mutable - address, email can change over time
 * - Same MemberId = same member, even if properties change
 * - Can be tracked across time
 */
@Entity
@Table(name = "members")
public class Member {
    
    @EmbeddedId
    private MemberId id;
    
    @Column(nullable = false)
    private String name;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true))
    private Email email;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
        @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private Address address;
    
    @Column(nullable = false)
    private LocalDate memberSince;
    
    @Column(nullable = false)
    private boolean active;
    
    // JPA requires default constructor
    protected Member() {}
    
    public Member(MemberId id, String name, Email email, Address address) {
        if (id == null) {
            throw new IllegalArgumentException("MemberId cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.memberSince = LocalDate.now();
        this.active = true;
    }
    
    // Business methods
    public void updateAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        // Value objects are immutable, so we replace the entire address
        this.address = newAddress;
    }
    
    public void updateEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = newEmail;
    }
    
    public void deactivate() {
        if (!active) {
            throw new IllegalStateException("Member is already inactive");
        }
        this.active = false;
    }
    
    public void activate() {
        if (active) {
            throw new IllegalStateException("Member is already active");
        }
        this.active = true;
    }
    
    // Getters
    public MemberId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public LocalDate getMemberSince() {
        return memberSince;
    }
    
    public boolean isActive() {
        return active;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        // Entities are equal if they have the same ID
        return id != null && id.equals(member.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return String.format("Member[id=%s, name=%s, email=%s, active=%s]", 
            id, name, email, active);
    }
}

