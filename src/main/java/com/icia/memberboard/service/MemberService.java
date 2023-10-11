package com.icia.memberboard.service;

import com.icia.memberboard.dto.MemberDTO;
import com.icia.memberboard.entity.MemberEntity;
import com.icia.memberboard.repository.MemberFileRepository;
import com.icia.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberFileRepository memberFileRepository;

    public boolean emailCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public Long save(MemberDTO memberDTO) throws IOException {
        if(memberDTO.getMemberFile().get(0).isEmpty()){
            //첨부 파일이 없는 경우임
            MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
            return memberRepository.save(memberEntity).getId();
        }else{
            //첨부 파일이 있는 경우
            MemberEntity memberEntity = MemberEntity.toSaveEntityWithFile(memberDTO);
            // 게시글 저장 후 저장한 "엔티티" 가져오기
            MemberEntity savedEntity = memberRepository.save(memberEntity);
            // 파일 이름 처리, 파일 로컬에 저장 등

        }
    }
}
