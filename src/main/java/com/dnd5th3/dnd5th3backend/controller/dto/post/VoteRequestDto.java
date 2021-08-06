package com.dnd5th3.dnd5th3backend.controller.dto.post;

import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VoteRequestDto {

    private VoteType result;
}
