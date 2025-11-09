package be.ucll.craftmanship.DDDDemo.library.application.dto;

import be.ucll.craftmanship.DDDDemo.library.domain.entities.Member;

/**
 * Data Transfer Object for Member responses
 */
public record MemberResponse(
    String id,
    String name,
    String email,
    String address,
    String memberSince,
    boolean active
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId().toString(),
            member.getName(),
            member.getEmail().toString(),
            member.getAddress().getFullAddress(),
            member.getMemberSince().toString(),
            member.isActive()
        );
    }
}

