package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.post.AllPostResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.exception.NoAuthorizationException;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.utils.RandomNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {

    private final PostsRepository postsRepository;

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
        Posts foundPost = postsRepository.findPostsById(id);
        foundPost.updateVoteStatusAndPostStatus();
        foundPost.increaseRankCount();

        return foundPost;
    }

    public Posts updatePost(Long id, String title, String content, String productImageUrl) {
        Posts foundPost = postsRepository.findPostsById(id);
        foundPost.update(title, content, productImageUrl);

        return foundPost;
    }

    public void deletePost(Long id, Member member) {
        Posts foundPost = postsRepository.findPostsById(id);
        if (foundPost.getMember().getId() != member.getId()) {
            throw new NoAuthorizationException("?????? ?????? ??????");
        }
        postsRepository.delete(foundPost);
    }

    public AllPostResponseDto findAllPostsWithSortType(String sortType) {
        List<Posts> postsList = postsRepository.findPostsWithSortType(sortType);
        postsList.forEach(post -> post.updateVoteStatusAndPostStatus());
        List<PostsListDto> listDtos = PostsListDto.makePostsToListDtos(postsList);
        return AllPostResponseDto.builder().listDtos(listDtos).build();
    }

    public Map<String, Posts> findMainPosts() {
        Map<String, Posts> resultMap = new HashMap<>();
        //?????? 50??? ????????? ??????
        List<Posts> top50RankedList = postsRepository.findPostsTop50Ranked();
        top50RankedList.stream().forEach(p -> {
            p.updateVoteStatusAndPostStatus();
        });
        //????????? 50??? ??? ??????(??????)??? ?????? ????????? ?????????
        List<Posts> filterByCommentCount = new ArrayList<>();
        top50RankedList.stream().forEach(p -> {
            if (p.getComments().size() != 0) {
                filterByCommentCount.add(p);
            }
        });
        //????????? ?????????
        Posts bestResponsePost;
        if (filterByCommentCount.size() >= 2) {
            //???????????? ??????????????? ???????????? ??????
            filterByCommentCount.stream().sorted((p1, p2) -> p2.getComments().size() - p1.getComments().size());
            //???????????? 5??? ???????????? ?????? 5??? ??? ???????????? ??????
            bestResponsePost = filterByCommentCount.size() >= 5
                    ? filterByCommentCount.get(RandomNumber.startFromZeroTo(5))
                    : filterByCommentCount.get(RandomNumber.startFromZeroTo(filterByCommentCount.size()));
        } else if (filterByCommentCount.size() == 1) {
            bestResponsePost = filterByCommentCount.get(0);
        } else {
            bestResponsePost = null;
        }
        //???????????? ??????
        List<Posts> neckAndNeckPostList = new ArrayList<>();
        top50RankedList.stream().forEach(p -> {
            VoteRatioVo ratioVo = new VoteRatioVo(p);
            if ((ratioVo.getRejectRatio() != 0 && ratioVo.getPermitRatio() != 0) && Math.abs(ratioVo.getPermitRatio() - ratioVo.getRejectRatio()) <= 10) {
                neckAndNeckPostList.add(p);
            }
        });
        //???????????? ?????????
        Posts neckAndNeckPost = neckAndNeckPostList.size() == 0 ? null : neckAndNeckPostList.get(RandomNumber.startFromZeroTo(neckAndNeckPostList.size()));
        //????????? ?????????
        Posts hotPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));
        //?????? ?????? ?????????
        Posts belovedPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));
        //????????? ?????????
        Posts recommendPost = top50RankedList.get(RandomNumber.startFromZeroTo(50));

        //?????? ?????? ???????????? ??????
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
}
