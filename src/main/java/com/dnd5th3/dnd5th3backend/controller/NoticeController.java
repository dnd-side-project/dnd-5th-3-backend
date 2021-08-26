package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.notice.NoticeListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.notice.NoticeResponseDto;
import com.dnd5th3.dnd5th3backend.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    @GetMapping
    public ResponseEntity<NoticeListResponseDto> getNoticeListAPI(){
        List<NoticeResponseDto> noticeResponseDtoList = noticeRepository.getAllNotice();
        return ResponseEntity.ok(new NoticeListResponseDto(noticeResponseDtoList));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNoticeAPI(@PathVariable Long noticeId){
        NoticeResponseDto notice = noticeRepository.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

}
