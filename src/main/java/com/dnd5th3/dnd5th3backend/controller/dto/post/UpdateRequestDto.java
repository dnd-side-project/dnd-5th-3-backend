package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateRequestDto {

    private String title;
    private String content;
    private String productImageUrl;
}
