package be.ucll.craftmanship.DDDDemo.library.domain.repositories;

import be.ucll.craftmanship.DDDDemo.library.domain.entities.Member;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Email;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Member Entity
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    
    /**
     * Finds a member by email
     * Emails are unique per member
     */
    Optional<Member> findByEmail(Email email);
    
    /**
     * Finds all active members
     */
    List<Member> findByActiveTrue();
    
    /**
     * Searches members by name (case-insensitive, partial match)
     */
    List<Member> findByNameContainingIgnoreCase(String name);
    
    /**
     * Checks if a member with given email exists
     */
    boolean existsByEmail(Email email);
}

