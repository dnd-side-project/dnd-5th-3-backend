package com.dnd5th3.dnd5th3backend.controller.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    RANK_COUNT("rank-count"),
    CREATED_DATE("created-date"),
    ALREADY_DONE("already-done"),
    ALMOST_DONE("almost-done");

    private final String value;
}
