package com.dnd5th3.dnd5th3backend.scheduler;

import com.dnd5th3.dnd5th3backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberScheduler {

    private final MemberService memberService;
    private static final long WITHDRAWAL_MEMBER_MAINTENANCE_PERIOD = 0;

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteWithdrawalMember(){
        memberService.deleteWithdrawalMember(WITHDRAWAL_MEMBER_MAINTENANCE_PERIOD);
    }
}
