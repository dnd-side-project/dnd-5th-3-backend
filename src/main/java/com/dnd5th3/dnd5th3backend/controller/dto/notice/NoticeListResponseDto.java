package com.dnd5th3.dnd5th3backend.controller.dto.notice;


import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListResponseDto {

    private List<NoticeResponseDto> noticeList;

}
