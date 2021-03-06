package com.dnd5th3.dnd5th3backend.domain.member;

import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Member extends BaseTime {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Setter
    private String refreshToken;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Posts> postsList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Comment> commentList;

    public static Member create(MemberRequestDto memberRequestDto,String encodePassword){

        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encodePassword)
                .name(memberRequestDto.getName())
                .memberType(MemberType.GENERAL)
                .role(Role.ROLE_USER)
                .build();
    }

    public void update(String name,String encodePassword){
        if(name != null){
          this.name = name;
        }
        if(encodePassword != null){
           this.password = encodePassword;
        }
    }

    public void updateMemberType(MemberType memberType){
       this.memberType = memberType;
    }
}
