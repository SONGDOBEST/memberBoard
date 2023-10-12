package com.icia.memberboard.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "member_file_table")
public class MemberFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id") // DB에 생성될 참조 칼럼의 이름
    private MemberEntity memberEntity; // 부모 엔티티 타입으로 정의

    public static MemberFileEntity toSaveMemberFile(MemberEntity savedEntity, String originalFileName, String storedFileName){
        MemberFileEntity memberFileEntity = new MemberFileEntity();
        memberFileEntity.setOriginalFileName(originalFileName);
        memberFileEntity.setStoredFileName(storedFileName);
        memberFileEntity.setMemberEntity(savedEntity);
        return memberFileEntity;
    }
}
