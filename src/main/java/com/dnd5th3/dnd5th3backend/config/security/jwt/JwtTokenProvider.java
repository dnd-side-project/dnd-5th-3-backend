package com.dnd5th3.dnd5th3backend.config.security.jwt;

import com.dnd5th3.dnd5th3backend.config.security.CustomAuthenticationToken;
import com.dnd5th3.dnd5th3backend.config.security.MemberContext;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.exception.MemberNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String SECRET;
    private final long ACCESS_TOKEN_EXPIRED_TIME;
    private final long REFRESH_TOKEN_EXPIRED_TIME;
    private final UserDetailsService userDetailsService;
    private final MemberRepository memberRepository;

    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret
            , @Value("${jwt.access-token-expired-time}") long accessTokenExpiredTime
            , @Value("${jwt.refresh-token-expired-time}") long refreshTokenExpiredTime
            , @Autowired UserDetailsService userDetailsService
            , @Autowired MemberRepository memberRepository) {

        this.SECRET = secret;
        this.ACCESS_TOKEN_EXPIRED_TIME = accessTokenExpiredTime;
        this.REFRESH_TOKEN_EXPIRED_TIME = refreshTokenExpiredTime;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.userDetailsService = userDetailsService;
        this.memberRepository = memberRepository;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    private String createToken(Authentication authentication,long expiredTime,boolean isRefreshToken){
        Member member = (Member)authentication.getPrincipal();
        Claims claims = Jwts.claims();
        if(isRefreshToken){
            UUID uuid = UUID.randomUUID();
            claims.put("refresh",uuid.toString());
        }
        return Jwts.builder()
                .setSubject(member.getEmail())
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .claim("claim",claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .compact();
    }

    public String createRefreshToken(Authentication authentication){
        String refreshToken = createToken(authentication, REFRESH_TOKEN_EXPIRED_TIME, true);
        saveRefreshToken(authentication,refreshToken);
        return createToken(authentication,REFRESH_TOKEN_EXPIRED_TIME,true);
    }

    public String createAccessToken(Authentication authentication){
      return createToken(authentication,ACCESS_TOKEN_EXPIRED_TIME,false);
    }

    public void saveRefreshToken(Authentication authentication,String refreshToken){
        Member member = (Member)authentication.getPrincipal();
        Member targetMember = memberRepository.findById(member.getId())
                .orElseThrow(()->new MemberNotFoundException("save RefreshToken Error"));
        targetMember.setRefreshToken(refreshToken);
        //TODO: Dirty checking 방식으로 수정필요
        memberRepository.save(targetMember);
    }

    public boolean isVaildToken(String token){
        try{
            Date expiredDate = jwtParser.parseClaimsJws(token).getBody().getExpiration();
            Date now = new Date();
            return  expiredDate.after(now);
        }catch (ExpiredJwtException e){
            log.error("토큰이 만료되었습니다.");
        }catch (Exception e){
            log.error("토큰 파싱 오류발생");
            e.printStackTrace();
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        String memberEmail = jwtParser.parseClaimsJws(token).getBody().getSubject();
        MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(memberEmail);
        return new CustomAuthenticationToken(memberContext.getMember(),null,memberContext.getAuthorities());
    }
}

