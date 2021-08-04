package com.dnd5th3.dnd5th3backend.controller.dto.post;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CalculateRatioDto {

    private Long permitRatio;
    private Long rejectRatio;

    public static CalculateRatioDto calculate(Posts post) {
        Integer permitCount = post.getPermitCount();
        Integer rejectCount = post.getRejectCount();
        Long permitRatio = Math.round(((double) permitCount / (permitCount + rejectCount)) * 100);
        Long rejectRatio = Math.round(((double) rejectCount / (permitCount + rejectCount)) * 100);

        return CalculateRatioDto.builder().permitRatio(permitRatio).rejectRatio(rejectRatio).build();
    }
}
