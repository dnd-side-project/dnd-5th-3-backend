package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AllResponseDto<T> {

    private T posts;
}
