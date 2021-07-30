package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostSaveResponseDto {

    private String name;
    private String title;
    private String productName;
    private String content;
    private String productImageUrl;
    private int permitRatio;
    private int rejectRatio;
    private LocalDateTime createdDate;
}
