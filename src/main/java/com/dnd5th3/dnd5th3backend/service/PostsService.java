package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.post.AllPostResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.SortType;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.exception.NoAuthorizationException;
import com.dnd5th3.dnd5th3backend.exception.PostNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import com.dnd5th3.dnd5th3backend.utils.RandomNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final VoteRepository voteRepository;

    public Posts savePost(Member member, String title, String content, String productImageUrl) {
        Posts newPosts = Posts.builder()
                .member(member)
                .title(title)
                .content(content)
                .productImageUrl(productImageUrl)
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(0)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .postsDeadline(LocalDateTime.now().plusDays(7L))
                .build();
        return postsRepository.save(newPosts);
    }

    public Posts findPostById(Long id) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        updateVoteStatusAndPostStatus(foundPost);
        //조회수 증가
        foundPost.increaseRankCount();

        return foundPost;
    }

    public Posts updatePost(Long id, String title, String content, String productImageUrl) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        foundPost.update(title, content, productImageUrl);

        return foundPost;
    }

    public void deletePost(Long id, Member member) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        if (foundPost.getMember().getId() != member.getId()) {
            throw new NoAuthorizationException("삭제 권한 없음");
        }
        postsRepository.delete(foundPost);
    }

    public List<Posts> findAllPosts(String sorted) {
        List<Posts> allPosts = postsRepository.findAll();
        //프록시 객체 초기화, 투표 종료 여부 초기화
        allPosts.stream().forEach(p -> {
            Hibernate.initialize(p.getMember());
            updateVoteStatusAndPostStatus(p);
        });

        //인기순
        if (SortType.RANK_COUNT.getValue().equals(sorted)) {
            List<Posts> allPostsOrderByRankCount = postsRepository.findPostsOrderByRankCount();
            return allPostsOrderByRankCount;
        }
        //최신순
        if (SortType.CREATED_DATE.getValue().equals(sorted)) {
            List<Posts> allPostsOrderByCreatedDate = postsRepository.findPostsOrderByCreatedDate();
            return allPostsOrderByCreatedDate;
        }
        //최근마감순
        if (SortType.ALREADY_DONE.getValue().equals(sorted)) {
            List<Posts> allPostsOrderByAlreadyDone = postsRepository.findPostsOrderByAlreadyDone();
            return allPostsOrderByAlreadyDone;
        }
        //마감임박순
        if (SortType.ALMOST_DONE.getValue().equals(sorted)) {
            List<Posts> allPostsOrderByAlmostDone = postsRepository.findPostsOrderByAlmostDone();
            return allPostsOrderByAlmostDone;
        }

        return allPosts;
    }

    public AllPostResponseDto findAllPostsWithSortType(String sortType) {
        List<Posts> postsList = postsRepository.findPostsWithSortType(sortType);
        postsList.forEach(posts -> updateVoteStatusAndPostStatus(posts));
        List<PostsListDto> listDtos = makePostsToDtos(postsList);
        return AllPostResponseDto.builder().listDtos(listDtos).build();
    }

    public Map<String, Posts> findMainPosts() {
        Map<String, Posts> resultMap = new HashMap<>();
        //상위 50개 컨텐츠 추출
        List<Posts> top50RankedList = postsRepository.findPostsTop50Ranked();
        //프록시 객체 초기화, 투표 종료 여부 초기화
        top50RankedList.stream().forEach(p -> {
            Hibernate.initialize(p.getMember());
            Hibernate.initialize(p.getComments());
            updateVoteStatusAndPostStatus(p);
        });
        //추출한 50개 중 반응(댓글)이 있는 것들만 필터링
        List<Posts> filterByCommentCount = new ArrayList<>();
        top50RankedList.stream().forEach(p -> {
            if (p.getComments().size() != 0) {
                filterByCommentCount.add(p);
            }
        });
        //최고의 반응글
        Posts bestResponsePost;
        if (filterByCommentCount.size() >= 2) {
            //필터링한 게시글들을 댓글수로 정렬
            filterByCommentCount.stream().sorted((p1, p2) -> p2.getComments().size() - p1.getComments().size());
            //게시글이 5개 이상이면 상위 5개 중 랜덤으로 추출
            bestResponsePost = filterByCommentCount.size() >= 5
                    ? filterByCommentCount.get(RandomNumber.startFromZeroTo(5))
                    : filterByCommentCount.get(RandomNumber.startFromZeroTo(filterByCommentCount.size()));
        } else if (filterByCommentCount.size() == 1) {
            bestResponsePost = filterByCommentCount.get(0);
        } else {
            bestResponsePost = null;
        }
        //투표비율 비교
        List<Posts> neckAndNeckPostList = new ArrayList<>();
        top50RankedList.stream().forEach(p -> {
            VoteRatioVo ratioVo = new VoteRatioVo(p);
            if ((ratioVo.getRejectRatio() != 0 && ratioVo.getPermitRatio() != 0) && Math.abs(ratioVo.getPermitRatio() - ratioVo.getRejectRatio()) <= 10) {
                neckAndNeckPostList.add(p);
            }
        });
        //막상막하 투표글
        Posts neckAndNeckPost = neckAndNeckPostList.size() == 0 ? null : neckAndNeckPostList.get(RandomNumber.startFromZeroTo(neckAndNeckPostList.size()));
        //불타고 있는글
        Posts hotPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));
        //사랑 듬뿍 받은글
        Posts belovedPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));
        //무물의 추천글
        Posts recommendPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));

        //값이 있는 데이터만 전달
        if (bestResponsePost != null) {
            resultMap.put("bestResponsePost", bestResponsePost);
        }
        if (neckAndNeckPost != null) {
            resultMap.put("neckAndNeckPost", neckAndNeckPost);
        }
        resultMap.put("hotPost", hotPost);
        resultMap.put("belovedPost", belovedPost);
        resultMap.put("recommendPost", recommendPost);

        return resultMap;
    }

    private void updateVoteStatusAndPostStatus(Posts posts) {
        //투표 종료 여부
        if (Boolean.FALSE.equals(posts.getIsVoted()) && LocalDateTime.now().isAfter(posts.getVoteDeadline())) {
            posts.makeVotedStatusTrue();
        }
        //메인페이지 게시 조건 종료 여부
        if (Boolean.FALSE.equals(posts.getIsPostsEnd()) && LocalDateTime.now().isAfter(posts.getPostsDeadline())) {
            posts.makePostsEndStatusTrue();
        }
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
