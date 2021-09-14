package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.exception.TokenException;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.utils.EmailSender;
import com.dnd5th3.dnd5th3backend.utils.RandomNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final EmailSender emailSender;
    private final ModelMapper modelMapper;
    private static final int PAGE_SIZE = 3;


    public Member saveMember(MemberRequestDto memberRequestDto) {
        String encode = passwordEncoder.encode(memberRequestDto.getPassword());
        Member member = Member.create(memberRequestDto, encode);
        return memberRepository.save(member);
    }

    public MemberReissueTokenResponseDto reissueAccessToken(MemberRequestDto memberRequestDto){
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail());
        String refreshToken = member.getRefreshToken();
        String reissueAccessToken;

        if(memberRequestDto.getRefreshToken().equals(refreshToken)){
            if(jwtTokenProvider.isVaildToken(refreshToken)){
              reissueAccessToken = jwtTokenProvider.createAccessToken(member);
            }else {
                throw new TokenException();
            }
        }else {
            throw new IllegalArgumentException("refreshToken이 유효하지 않습니다.");
        }

        return  MemberReissueTokenResponseDto.builder()
                .email(member.getEmail())
                .accessToken(reissueAccessToken)
                .build();
    }

    public boolean isExistsEmail(String email){
      return memberRepository.existsByEmail(email);
    }

    public boolean isExistsName(String name){
      return memberRepository.existsByName(name);
    }

    public boolean isCollectPassword(String password,Member member){
        Member targetMember = memberRepository.findByEmail(member.getEmail());
        return passwordEncoder.matches(password,targetMember.getPassword());
    }

    @Transactional
    public Member updateMember(MemberRequestDto memberRequestDto, Member member) {
        Member targetMember = memberRepository.findByEmail(member.getEmail());
        if(memberRequestDto.getPassword() != null){
            String encode = passwordEncoder.encode(memberRequestDto.getPassword());
            targetMember.update(memberRequestDto.getName(),encode);
        }else {
            targetMember.update(memberRequestDto.getName(),memberRequestDto.getPassword());
        }
        return targetMember;
    }

    @Transactional
    public Member withdrawal(MemberRequestDto memberRequestDto, Member member) {
        if(memberRequestDto.getEmail().equals(member.getEmail())){
            Member targetMember = memberRepository.findByEmail(member.getEmail());
            List<Comment> commentList = targetMember.getCommentList();
            List<Posts> postsList = targetMember.getPostsList();

            commentRepository.deleteAll(commentList);
            postsRepository.deleteAll(postsList);
            targetMember.updateMemberType(MemberType.WITHDRAWAL);

            return targetMember;
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void resetPasswordMember(MemberRequestDto memberRequestDto) throws MessagingException {
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail());
        if(member != null){
            String tempPassword = RandomNumber.generatePassword();
            emailSender.sendTemporaryPassword(member,tempPassword);
            member.update(null,passwordEncoder.encode(tempPassword));
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void deleteWithdrawalMember(long termDay) {
        List<Member> memberList = memberRepository.findAllByMemberType(MemberType.WITHDRAWAL);
        for(Member member :  memberList){
            LocalDate withdrawalDay = member.getUpdatedDate().toLocalDate();
            long between = ChronoUnit.DAYS.between(withdrawalDay, LocalDate.now());
            if(between >= termDay){
                log.info(" 탈퇴한 회원 - [{}]",member.getEmail());
                memberRepository.delete(member);
            }
        }
    }

    @Transactional
    public MemberListResponseDto getPageMemberList(Pageable pageable){
        Pageable page = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<Member> memberPage = memberRepository.findAll(page);
        MemberListResponseDto map = modelMapper.map(memberPage.getContent(), MemberListResponseDto.class);

        MemberListResponseDto memberListResponseDto = new MemberListResponseDto();
        List<MemberListResponseDto.MemberDto> memberDtoList = new ArrayList<>();
        for(Member member : memberPage.getContent()){
            MemberListResponseDto.MemberDto memberDto = modelMapper.map(member, MemberListResponseDto.MemberDto.class);
            memberDtoList.add(memberDto);
        }
        memberListResponseDto.setMemberDtoList(memberDtoList);
        memberListResponseDto.setPageNum(memberPage.getNumber());
        memberListResponseDto.setTotalPage(memberPage.getTotalPages());
        memberListResponseDto.setTotalCount(memberPage.getTotalElements());
        return memberListResponseDto;
    }
}
