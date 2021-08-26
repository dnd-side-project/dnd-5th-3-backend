package com.dnd5th3.dnd5th3backend.domain.notice;

import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Notice extends BaseTime {

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

}
