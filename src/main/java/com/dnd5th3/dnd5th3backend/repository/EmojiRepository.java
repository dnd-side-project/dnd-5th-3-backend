package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmojiRepository extends JpaRepository<Emoji, Long> {
}
