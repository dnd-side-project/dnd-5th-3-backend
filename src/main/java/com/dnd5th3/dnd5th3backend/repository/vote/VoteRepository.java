package com.dnd5th3.dnd5th3backend.repository.vote;

import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> , VoteRepositoryCustom{
    @Query("SELECT v FROM Vote v WHERE v.posts.id = :postId")
    List<Vote> getAllByPostId(long postId);
}
