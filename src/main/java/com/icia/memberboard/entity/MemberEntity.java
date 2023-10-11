package com.icia.memberboard.entity;

import com.icia.memberboard.dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "member_table")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String memberEmail;

    @Column(length = 30, nullable = false)
    private String memberPassword;

    @Column(length = 20, nullable = false)
    private String memberName;

    @Column(length = 30, nullable = false, unique = true)
    private String memberMobile;

    @Column(nullable = false)
    private String memberBirth;

    @Column
    private int fileAttached;

    // 참조관계 정의
    // mappedBy: 자식 엔티티에 정의한 필드 이름
    // cascade, orphanRemoval: 부모 데이터 삭제시 자식 데이터도 삭제
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberFileEntity> memberFileEntityList = new ArrayList<>();


    public static MemberEntity toSaveEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberEntity.getMemberPassword());
        memberEntity.setMemberName(memberEntity.getMemberName());
        memberEntity.setMemberMobile(memberEntity.getMemberMobile());
        memberEntity.setMemberBirth(memberEntity.getMemberBirth());
        memberEntity.setFileAttached(0);

        return memberEntity;
    }

    public static MemberEntity toSaveEntityWithFile(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberEntity.getMemberPassword());
        memberEntity.setMemberName(memberEntity.getMemberName());
        memberEntity.setMemberMobile(memberEntity.getMemberMobile());
        memberEntity.setMemberBirth(memberEntity.getMemberBirth());
        memberEntity.setFileAttached(1);

        return memberEntity;
    }
}
