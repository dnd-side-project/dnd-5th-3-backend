package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import com.dnd5th3.dnd5th3backend.repository.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmojiService {

    private final EmojiRepository emojiRepository;

    public long saveEmoji(String detail){
        Emoji emoji = emojiRepository.save(Emoji.create(detail));
        return emoji.getId();
    }
}
