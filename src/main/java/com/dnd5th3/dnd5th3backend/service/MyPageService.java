package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.mypage.InfoResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.mypage.SortType;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MyPageService {

    private final PostsRepository postsRepository;
    private final VoteRepository voteRepository;

    public InfoResponseDto findMemberInfoWithSortType(Member member, String sorted) {
        List<PostsListDto> postsList = new ArrayList<>();
        /**
         * 내가 쓴 글
         */
        if (SortType.WRITTEN.getValue().equals(sorted)) {
            List<Posts> posts = postsRepository.findPostsByMemberOrderByCreatedDate(member);
            postsList = PostsListDto.makePostsToListDtos(posts);
        }
        /**
         * 투표한 글
         */
        if (SortType.VOTED.getValue().equals(sorted)) {
            List<Vote> votes = voteRepository.findVoteByMemberOrderByCreatedDate(member);
            List<Posts> posts = new ArrayList<>();
            votes.forEach(v -> {
                posts.add(v.getPosts());
            });
            postsList = PostsListDto.makePostsToListDtos(posts);
        }

        return InfoResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .postsList(postsList)
                .build();
    }
}
