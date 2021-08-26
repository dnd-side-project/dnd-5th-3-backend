package com.dnd5th3.dnd5th3backend.repository.notice;

import com.dnd5th3.dnd5th3backend.controller.dto.notice.NoticeResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.notice.QNoticeResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dnd5th3.dnd5th3backend.domain.notice.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory query;

   @Override
    public List<NoticeResponseDto> getAllNotice() {
        return  query.select(new QNoticeResponseDto(notice.id,notice.title,notice.content,notice.createdDate,notice.updatedDate))
                .from(notice)
                .orderBy(notice.id.desc())
                .fetch();
    }

    @Override
    public NoticeResponseDto getNotice(long noticeId) {
        return  query.select(new QNoticeResponseDto(notice.id,notice.title,notice.content,notice.createdDate,notice.updatedDate))
                .from(notice)
                .where(notice.id.eq(noticeId))
                .fetchOne();
    }
}
