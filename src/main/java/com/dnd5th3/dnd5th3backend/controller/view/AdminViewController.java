package com.dnd5th3.dnd5th3backend.controller.view;

import com.dnd5th3.dnd5th3backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final MemberService memberService;

    @GetMapping("/main")
    public void main(HttpServletRequest request, Model model){
        model.addAttribute("now", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        model.addAttribute("host",request.getRemoteHost());
    }

    @GetMapping("/members")
    public void members(@RequestParam(defaultValue = "0") Integer pageNum, Model model){
    }
}
