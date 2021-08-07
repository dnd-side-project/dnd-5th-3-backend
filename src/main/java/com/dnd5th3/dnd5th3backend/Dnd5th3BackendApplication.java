package com.dnd5th3.dnd5th3backend;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class Dnd5th3BackendApplication implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public static void main(String[] args) {
        SpringApplication.run(Dnd5th3BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO: 테스트 용도 main 가기전 제거
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        memberRepository.save(Member.builder().email("test@gmail.com")
                .password(delegatingPasswordEncoder.encode("1234"))
                .role(Role.ROLE_USER)
                .name("닉네임")
                .build());

    }
}
