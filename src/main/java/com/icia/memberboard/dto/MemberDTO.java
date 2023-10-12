package com.icia.memberboard.dto;

import com.icia.memberboard.entity.MemberEntity;
import com.icia.memberboard.entity.MemberFileEntity;
import com.icia.memberboard.util.UtilClass;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberMobile;
    private String createdAt;
    private String memberBirth;

    private List<MultipartFile> memberFile;
    private int fileAttached; //파일 첨부 여부
    private List<String> originalFileName = new ArrayList<>();
    private List<String> storedFileName = new ArrayList<>();

    public static MemberDTO toDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberMobile(memberEntity.getMemberMobile());
        memberDTO.setCreatedAt(UtilClass.dateTimeFormat(memberEntity.getCreatedAt()));
        memberDTO.setMemberBirth(memberEntity.getMemberBirth());

        //파일첨부 여부 확인 후 파일 가져가기
        if(memberEntity.getFileAttached() == 1){
            for(MemberFileEntity memberFileEntity : memberEntity.getMemberFileEntityList()){
                memberDTO.getOriginalFileName().add(memberFileEntity.getOriginalFileName());
                memberDTO.getStoredFileName().add(memberFileEntity.getStoredFileName());
            }
            memberDTO.setFileAttached(1);
        }else{
            memberDTO.setFileAttached(0);
        }

        return memberDTO;
    }
}
