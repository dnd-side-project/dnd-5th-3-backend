package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveRequestDto {

    private Long memberId;
    private String title;
    private String productName;
    private String content;
    private String productImageUrl;
}