package com.dnd5th3.dnd5th3backend.domain.vo;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.Getter;

import java.util.Objects;

@Getter
public class VoteRatioVo {

    private final Long permitRatio;
    private final Long rejectRatio;

    public VoteRatioVo(Posts posts) {
        Integer permitCount = posts.getPermitCount();
        Integer rejectCount = posts.getRejectCount();
        Long permitRatio = Math.round(((double) permitCount / (permitCount + rejectCount)) * 100);
        Long rejectRatio = Math.round(((double) rejectCount / (permitCount + rejectCount)) * 100);

        this.permitRatio = permitRatio;
        this.rejectRatio = rejectRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteRatioVo voteRatioVo = (VoteRatioVo) o;
        return Objects.equals(permitRatio, voteRatioVo.permitRatio) && Objects.equals(rejectRatio, voteRatioVo.rejectRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permitRatio, rejectRatio);
    }
}
