package com.dnd5th3.dnd5th3backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(EmojiService.class)
class EmojiServiceTest {

    @Autowired
    private EmojiService emojiService;

    @DisplayName("이모지 등록")
    @Test
    void saveEmoji() {
        String detail = "놀람";
        long emojiId = emojiService.saveEmoji(detail);
        assertNotEquals(0,emojiId,"이모지 등록 정상인지 확인");
    }
}