package com.dnd5th3.dnd5th3backend.controller.dto.mypage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    WRITTEN("written"),
    VOTED("voted");

    private final String value;
}
