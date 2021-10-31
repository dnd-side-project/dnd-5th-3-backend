package com.dnd5th3.dnd5th3backend.utils;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private static final String FROM = "moomool.official@gmail.com";

    public void sendTemporaryPassword(Member member,String temporaryPassword) throws MessagingException {

        Context context = new Context();
        context.setVariable("name",member.getName());
        context.setVariable("password",temporaryPassword);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        String html = templateEngine.process("mail.html", context);
        messageHelper.setFrom(FROM);
        messageHelper.setTo(member.getEmail());
        messageHelper.setSubject("[무물] 임시비밀번호 발급안내");
        messageHelper.setText(html,true);

        javaMailSender.send(mimeMessage);
    }
}
