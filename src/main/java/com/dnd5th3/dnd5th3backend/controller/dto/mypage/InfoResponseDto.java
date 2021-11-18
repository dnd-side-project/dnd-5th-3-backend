package com.dnd5th3.dnd5th3backend.controller.dto.mypage;

import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class InfoResponseDto {

    private String name;
    private String email;
    private List<PostsListDto> postsListDtos;
}
