package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberResponseDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<MemberResponseDto> signUpAPI(@RequestBody MemberRequestDto memberRequestDto){
        Member member = memberService.saveMember(memberRequestDto);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    @PutMapping("/token")
    public ResponseEntity<MemberReissueTokenResponseDto> reissueAPI(@RequestBody MemberRequestDto memberRequestDto){
        MemberReissueTokenResponseDto reissueTokenResponseDto = memberService.reissueAccessToken(memberRequestDto);
        return ResponseEntity.ok(reissueTokenResponseDto);
    }

    @GetMapping("/exists/{email}/email")
    public ResponseEntity<Boolean> existCheckAPI(@PathVariable String email){
        boolean existsEmail = memberService.isExistsEmail(email);
        return ResponseEntity.ok(existsEmail);
    }

    @GetMapping("/exists/{name}/name")
    public ResponseEntity<Boolean> existEmailAPI(@PathVariable String name){
        boolean existsName = memberService.isExistsName(name);
        return ResponseEntity.ok(existsName);
    }

    @PostMapping("/check/password")
    public ResponseEntity<Boolean> checkPasswordAPI(@RequestBody MemberRequestDto memberRequestDto,@AuthenticationPrincipal Member member){
        boolean existsName = memberService.isCollectPassword(memberRequestDto.getPassword(),member);
        return ResponseEntity.ok(existsName);
    }

    @PutMapping
    public ResponseEntity<MemberResponseDto> updateProfileAPI(@RequestBody MemberRequestDto memberRequestDto,@AuthenticationPrincipal Member member){
        Member updateMember = memberService.updateMember(memberRequestDto,member);
        MemberResponseDto memberResponseDto = modelMapper.map(updateMember, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }
}
