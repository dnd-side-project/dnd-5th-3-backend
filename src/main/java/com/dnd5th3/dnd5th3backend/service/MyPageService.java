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
        List<PostsListDto> postsListDtos = new ArrayList<>();
        /**
         * 내가 쓴 글
         */
        if (SortType.WRITTEN.getValue().equals(sorted)) {
            List<Posts> posts = postsRepository.findPostsByMemberOrderByCreatedDate(member);
            postsListDtos = makePostsToDtos(posts);
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
            postsListDtos = makePostsToDtos(posts);
        }

        return InfoResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .postsListDtos(postsListDtos)
                .build();
    }

    private List<PostsListDto> makePostsToDtos(List<Posts> posts) {
        return posts.stream().map(p -> {
            VoteRatioVo ratioVo = new VoteRatioVo(p);
            String productImageUrl = p.getProductImageUrl() == null ? "" : p.getProductImageUrl();
            return PostsListDto.builder()
                    .id(p.getId())
                    .name(p.getMember().getName())
                    .title(p.getTitle())
                    .productImageUrl(productImageUrl)
                    .isVoted(p.getIsVoted())
                    .permitRatio(ratioVo.getPermitRatio())
                    .rejectRatio(ratioVo.getRejectRatio())
                    .createdDate(p.getCreatedDate())
                    .voteDeadline(p.getVoteDeadline())
                    .build();
        }).collect(Collectors.toList());
    }
}
