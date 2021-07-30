package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
