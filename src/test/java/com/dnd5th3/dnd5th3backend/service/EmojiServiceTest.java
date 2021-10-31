package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.controller.dto.emoji.EmojiRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({EmojiService.class, QuerydslConfig.class})
@ActiveProfiles("h2")
class EmojiServiceTest {

    @Autowired
    private EmojiService emojiService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        long memberId = 1;
        member = memberRepository.getById(memberId);
    }


    @DisplayName("이모지 등록")
    @Test
    void saveEmoji() {
        String detail = "놀람";
        long emojiId = emojiService.saveEmoji(detail);
        assertNotEquals(0, emojiId, "이모지 등록 정상인지 확인");
    }


    @DisplayName("댓글 이모지 등록 & Count 업데이트 & 삭제")
    @Test
    void saveCommentEmoji_updateCountCommentEmoji() {
        long commentId = 1;
        long emojiId = 1;

        EmojiRequestDto emojiRequestDto = new EmojiRequestDto(commentId, emojiId, null, null);
        CommentEmoji commentEmoji = emojiService.saveCommentEmoji(emojiRequestDto, member);
        assertNotNull(commentEmoji.getId(), "이모지 정상 등록 확인");

        EmojiRequestDto checkedRequest = new EmojiRequestDto(null, null, commentEmoji.getId(), true);

        commentEmoji = emojiService.updateCountCommentEmoji(checkedRequest, member);
        assertEquals(2, commentEmoji.getCommentEmojiCount(),"이모지 Count 업데이트 확인");

        final EmojiRequestDto uncheckedRequest = new EmojiRequestDto(null, null, commentEmoji.getId(), false);
        emojiService.updateCountCommentEmoji(uncheckedRequest,member);
        emojiService.updateCountCommentEmoji(uncheckedRequest,member);

        assertThrows(NoSuchElementException.class,()->emojiService.updateCountCommentEmoji(uncheckedRequest,member),
                "이모지 1개에서 Unchecked 할 경우 댓글 이모지 삭제 확인");

    }

}