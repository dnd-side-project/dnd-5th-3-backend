package com.dnd5th3.dnd5th3backend.repository.notice;

import com.dnd5th3.dnd5th3backend.controller.dto.notice.NoticeResponseDto;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<NoticeResponseDto> getAllNotice();
    NoticeResponseDto getNotice(long noticeId);
}
