package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.exception.TokenException;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.utils.EmailSender;
import com.dnd5th3.dnd5th3backend.utils.RandomNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final EmailSender emailSender;

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
    public Member deleteMember(MemberRequestDto memberRequestDto,Member member) {
        if(memberRequestDto.getEmail().equals(member.getEmail())){
            Member targetMember = memberRepository.findByEmail(member.getEmail());
            List<Comment> commentList = targetMember.getCommentList();
            List<Posts> postsList = targetMember.getPostsList();

            commentRepository.deleteAll(commentList);
            postsRepository.deleteAll(postsList);
            memberRepository.delete(targetMember);

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
}
