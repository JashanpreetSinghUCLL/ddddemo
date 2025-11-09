package be.ucll.craftmanship.DDDDemo.library.api.controllers;

import be.ucll.craftmanship.DDDDemo.library.application.dto.CreateMemberRequest;
import be.ucll.craftmanship.DDDDemo.library.application.dto.MemberResponse;
import be.ucll.craftmanship.DDDDemo.library.application.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Member operations
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<MemberResponse>> getActiveMembers() {
        List<MemberResponse> members = memberService.getActiveMembers();
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable String memberId) {
        MemberResponse member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchByName(@RequestParam String name) {
        List<MemberResponse> members = memberService.searchMembersByName(name);
        return ResponseEntity.ok(members);
    }
}

