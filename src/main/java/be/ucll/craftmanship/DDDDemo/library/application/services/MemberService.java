package be.ucll.craftmanship.DDDDemo.library.application.services;

import be.ucll.craftmanship.DDDDemo.library.application.dto.CreateMemberRequest;
import be.ucll.craftmanship.DDDDemo.library.application.dto.MemberResponse;
import be.ucll.craftmanship.DDDDemo.library.domain.entities.Member;
import be.ucll.craftmanship.DDDDemo.library.domain.repositories.MemberRepository;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Address;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.Email;
import be.ucll.craftmanship.DDDDemo.library.domain.valueobjects.MemberId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * APPLICATION SERVICE: MemberService
 */
@Service
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    /**
     * Creates a new member
     */
    public MemberResponse createMember(CreateMemberRequest request) {
        Email email = new Email(request.email());
        
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Member with email " + email + " already exists");
        }
        
        Address address = new Address(
            request.street(),
            request.city(),
            request.postalCode(),
            request.country()
        );
        
        Member member = new Member(
            MemberId.generate(),
            request.name(),
            email,
            address
        );
        
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }
    
    /**
     * Gets all members
     */
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
            .stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all active members
     */
    public List<MemberResponse> getActiveMembers() {
        return memberRepository.findByActiveTrue()
            .stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets a member by ID
     */
    public MemberResponse getMemberById(String memberId) {
        Member member = memberRepository.findById(MemberId.from(memberId))
            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        return MemberResponse.from(member);
    }
    
    /**
     * Searches members by name
     */
    public List<MemberResponse> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());
    }
}

