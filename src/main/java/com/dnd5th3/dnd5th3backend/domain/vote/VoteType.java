package com.dnd5th3.dnd5th3backend.domain.vote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteType {

    PERMIT("찬성"),
    REJECT("반대"),
    NO_RESULT("투표 미실시");

    private final String value;
}
