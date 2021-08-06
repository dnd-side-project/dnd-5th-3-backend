package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.service.EmojiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/emoji")
public class EmojiController {

    private final EmojiService emojiService;

    @GetMapping("/{detail}")
    public long saveAPI(@PathVariable String detail){
        return emojiService.saveEmoji(detail);
    }
}
