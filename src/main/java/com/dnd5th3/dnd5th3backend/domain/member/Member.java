package com.dnd5th3.dnd5th3backend.domain.member;

import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String password;

    @Column(unique = true)
    String email;

    @Enumerated(value = EnumType.STRING)
    Role role = Role.USER;
}
