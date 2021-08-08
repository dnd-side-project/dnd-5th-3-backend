package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.emoji.EmojiRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.emoji.EmojiResponseDto;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.service.EmojiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/emoji")
public class EmojiController {

    private final EmojiService emojiService;

    @GetMapping("/{detail}")
    public long saveAPI(@PathVariable String detail){
        return emojiService.saveEmoji(detail);
    }

    @PostMapping
    public ResponseEntity<EmojiResponseDto> saveAPI(@RequestBody EmojiRequestDto emojiRequestDto, @AuthenticationPrincipal Member member){
        CommentEmoji commentEmoji = emojiService.saveCommentEmoji(emojiRequestDto, member);
        EmojiResponseDto responseDto = EmojiResponseDto.builder()
                    .commentEmojiId(commentEmoji.getId())
                    .build();

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<EmojiResponseDto> updateAPI(@RequestBody EmojiRequestDto emojiRequestDto, @AuthenticationPrincipal Member member){
        CommentEmoji commentEmoji = emojiService.updateCountCommentEmoji(emojiRequestDto,member);
        EmojiResponseDto responseDto = EmojiResponseDto.builder()
                .commentEmojiId(commentEmoji.getId())
                .commentEmojiCount(commentEmoji.getCommentEmojiCount())
                .build();

        return ResponseEntity.ok(responseDto);
    }
}
