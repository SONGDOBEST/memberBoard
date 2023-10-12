package com.icia.memberboard.service;

import com.icia.memberboard.dto.MemberDTO;
import com.icia.memberboard.entity.MemberEntity;
import com.icia.memberboard.entity.MemberFileEntity;
import com.icia.memberboard.repository.MemberFileRepository;
import com.icia.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

    public Long save(MemberDTO memberDTO) throws IOException{
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
            // "멀티 파트 파일 리스트"에 담긴 데이터 꺼내오기 ==> 리스트 이므로 for문 돌려서 꺼내면 됨
            for(MultipartFile memberFile : memberDTO.getMemberFile()){
                //업로드 파일 이름
                String originalFileName= memberFile.getOriginalFilename();
                //저장 파일 이름
                String storedFileName= System.currentTimeMillis() + "_" + originalFileName;
                //저장경로 + 파일이름 준비
                String savePath = "D:\\memberFile\\" + storedFileName;
                //경로에 파일 저장
                memberFile.transferTo(new File(savePath));
                //파일 정보 "member_file_table"에 저장
                //파일 정보 저장을 위한 "boardFileEntity" 생성
                MemberFileEntity memberFileEntity =
                        MemberFileEntity.toSaveMemberFile(savedEntity, originalFileName, storedFileName);
                memberFileRepository.save(memberFileEntity);
            }
            return  savedEntity.getId();
        }
    }

    public MemberDTO login(MemberDTO memberDTO) {
        MemberDTO memberDTO1 = new MemberDTO();
        /*
            DB에서 로그인하는 사용자의 이메일로 조회한 결과를 가져와서
            비밀번호가 일치하는지 비교한 뒤 로그인 성공 여부를 판단

            findByMemberEmail()
            select * from member_table where member_email = ?

            findById()
            => select * from member_table where id = ?
         */

        //email과 password 둘 다 만족하는 결과가 있으면 로그인 성공, 아니면 실패
        Optional<MemberEntity> optionalMemberEntity =
                memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
        if(optionalMemberEntity.isPresent()){
            MemberEntity memberEntity = optionalMemberEntity.orElseThrow(()-> new NoSuchElementException());
            return memberDTO1.toDTO(memberEntity);
        }else{
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return MemberDTO.toDTO(memberEntity);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
