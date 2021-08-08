package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private static final int PAGE_SIZE = 50;

    public Comment saveComment(CommentRequestDto requestDto, Member member){
        Posts posts = postsRepository.findById(requestDto.getPostId()).orElseThrow();
        Comment comment = Comment.create(requestDto, member, posts);
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
    public CommentListResponseDto getCommentList(long postId,int pageNum, Member member){
        PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
        Page<Comment> pagingComment = commentRepository.getAllCommentList(postId,pageRequest);
        List<Comment> commentList = pagingComment.getContent();

        List<CommentListResponseDto.CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            CommentListResponseDto.CommentDto commentDto = modelMapper.map(comment, CommentListResponseDto.CommentDto.class);
            List<CommentListResponseDto.EmojiIDto> emojiIDtoList = new ArrayList<>();

            for(CommentEmoji commentEmoji1 : comment.getCommentEmoji()){

                CommentListResponseDto.EmojiIDto emojiIDto = new CommentListResponseDto.EmojiIDto();
                emojiIDto.setEmojiId(commentEmoji1.getEmoji().getId());
                emojiIDto.setEmojiCount(commentEmoji1.getCommentEmojiCount());
                List<CommentEmojiMember> commentEmojiMembers = commentEmoji1.getCommentEmojiMembers();

                boolean isChecked = false;
                if(commentEmojiMembers != null){
                    for(CommentEmojiMember emojiMember : commentEmojiMembers){
                        if(Objects.equals(emojiMember.getMember().getId(), member.getId())){
                            isChecked = true;
                            break;
                        }
                    }
                }
                emojiIDto.setChecked(isChecked);
                emojiIDtoList.add(emojiIDto);
            }
            commentDto.setEmojiList(emojiIDtoList);
            commentDtoList.add(commentDto);
        }

        return CommentListResponseDto.builder()
                .commentList(commentDtoList)
                .totalPage(pagingComment.getTotalPages())
                .totalCount(pagingComment.getTotalElements())
                .pageNum(pagingComment.getNumber())
                .build();
    }
}
