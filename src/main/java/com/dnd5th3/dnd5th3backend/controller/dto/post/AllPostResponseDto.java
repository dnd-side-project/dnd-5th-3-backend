package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllPostResponseDto {

    List<PostsListDto> listDtos;
}
