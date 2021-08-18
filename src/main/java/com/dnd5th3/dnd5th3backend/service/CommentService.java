package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final VoteRepository voteRepository;
    private static final int LOWER_COMMENT = 1;
    private static final int PAGE_SIZE = 50;
    private static final int TOP = 0;

    @Transactional
    public Comment saveComment(CommentRequestDto requestDto, Member member){
        Posts posts = postsRepository.findById(requestDto.getPostId()).orElseThrow();
        long nextGroupNo = commentRepository.nextGroupNo(requestDto.getPostId(), requestDto.getCommentLayer());
        Comment comment = Comment.create(requestDto,nextGroupNo, requestDto.getCommentLayer(),TOP,member, posts);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment saveReplyComment(CommentRequestDto requestDto, long commentId ,Member member){
        Comment topComment = commentRepository.findById(commentId).orElseThrow();
        Posts posts = topComment.getPosts();
        long groupNo = topComment.getGroupNo();
        int nextCommentOrder = commentRepository.nextCommentOrder(commentId,groupNo,requestDto.getCommentLayer());
        Comment comment = Comment.create(requestDto, groupNo, requestDto.getCommentLayer(), nextCommentOrder, member, posts);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment editComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId()).orElseThrow();
        comment.update(requestDto);
        return comment;
    }

    @Transactional
    public Comment deleteComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId()).orElseThrow();
        comment.delete();
        return comment;
    }

    @Transactional
    public CommentListResponseDto getDetailComment(long commentId){
        Comment requestComment = commentRepository.findById(commentId).orElseThrow();
        List<Comment> commentList = commentRepository.getCommentGroup(requestComment.getPosts().getId(), requestComment.getGroupNo());
        List<CommentListResponseDto.CommentDto> commentDtoList = new ArrayList<>();

        List<Vote> voteList = voteRepository.getAllByPostId(requestComment.getPosts().getId());
        Map<Member, Vote> votedMemberMap = voteList.stream().collect(Collectors.toMap(Vote::getMember, vote -> vote));

        for (Comment comment : commentList){
            CommentListResponseDto.CommentDto commentDto = convertCommentDto(votedMemberMap, comment);
            commentDtoList.add(commentDto);
        }

        return CommentListResponseDto.builder()
                .commentList(commentDtoList)
                .totalCount(commentDtoList.size())
                .build();
    }

    @Transactional
    public CommentListResponseDto getCommentList(long postId,int pageNum, Member member){
        PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
        Page<Comment> pagingComment = commentRepository.getAllCommentList(postId,pageRequest);
        List<Comment> commentList = pagingComment.getContent();
        List<CommentListResponseDto.CommentDto> commentDtoList = new ArrayList<>();

        List<Vote> voteList = voteRepository.getAllByPostId(postId);
        Map<Member, Vote> votedMemberMap = voteList.stream().collect(Collectors.toMap(Vote::getMember, vote -> vote));

        for(Comment comment : commentList){
            CommentListResponseDto.CommentDto commentDto =  convertCommentDto(votedMemberMap,comment);
            commentDto.setReplyCount(commentRepository.countByGroupNoAndCommentLayer(comment.getGroupNo(),LOWER_COMMENT));

            List<CommentListResponseDto.EmojiIDto> emojiIDtoList = new ArrayList<>();

            for(CommentEmoji commentEmoji1 : comment.getCommentEmoji()){
                CommentListResponseDto.EmojiIDto emojiIDto = new CommentListResponseDto.EmojiIDto();
                emojiIDto.setEmojiId(commentEmoji1.getEmoji().getId());
                emojiIDto.setEmojiCount(commentEmoji1.getCommentEmojiCount());
                setIsCheckedEmoji(member, commentEmoji1, emojiIDto);
                emojiIDtoList.add(emojiIDto);
            }

            commentDto.setEmojiList(emojiIDtoList);
            commentDtoList.add(commentDto);
        }
        commentDtoList.sort(Comparator.comparing(CommentListResponseDto.CommentDto::getCommentId));
        return CommentListResponseDto.builder()
                .commentList(commentDtoList)
                .totalPage(pagingComment.getTotalPages())
                .totalCount(pagingComment.getTotalElements())
                .pageNum(pagingComment.getNumber())
                .build();
    }

    private CommentListResponseDto.CommentDto convertCommentDto(Map<Member, Vote> votedMemberMap, Comment comment) {
        CommentListResponseDto.CommentDto commentDto = modelMapper.map(comment, CommentListResponseDto.CommentDto.class);
        Member writer = comment.getMember();
        commentDto.setMemberId(writer.getId());
        commentDto.setWriterName(writer.getName());
        commentDto.setEmail(writer.getEmail());

        if(votedMemberMap.containsKey(writer)){
            Vote vote = votedMemberMap.get(writer);
            commentDto.setVoteType(vote.getResult());
        }else {
            commentDto.setVoteType(VoteType.NO_RESULT);
        }

        return commentDto;
    }

    private void setIsCheckedEmoji(Member member, CommentEmoji commentEmoji1, CommentListResponseDto.EmojiIDto emojiIDto) {

        List<CommentEmojiMember> commentEmojiMemberList = commentEmoji1.getCommentEmojiMembers();
        Set<Long> commentEmojiMemberSet = commentEmojiMemberList.stream()
                .map(commentEmojiMember -> commentEmojiMember.getMember().getId())
                .collect(Collectors.toSet());
        emojiIDto.setChecked(commentEmojiMemberSet.contains(member.getId()));
    }

}
