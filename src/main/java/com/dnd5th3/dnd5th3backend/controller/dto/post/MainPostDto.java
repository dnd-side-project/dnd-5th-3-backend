package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MainPostDto {

    private Long id;
    private String name;
    private String title;
    private String productImageUrl;
    private Boolean isVoted;
    private Long permitRatio;
    private Long rejectRatio;
    private LocalDateTime createdDate;
    private LocalDateTime voteDeadline;
}
