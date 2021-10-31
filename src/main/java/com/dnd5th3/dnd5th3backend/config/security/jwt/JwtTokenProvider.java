package com.dnd5th3.dnd5th3backend.config.security.jwt;

import com.dnd5th3.dnd5th3backend.config.security.CustomAuthenticationToken;
import com.dnd5th3.dnd5th3backend.config.security.MemberContext;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.SECRET));
        this.userDetailsService = userDetailsService;
        this.memberRepository = memberRepository;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    private String createToken(Member member,long expiredTime,boolean isRefreshToken){
        if(isRefreshToken){
            Claims claims = Jwts.claims();
            UUID uuid = UUID.randomUUID();
            claims.put("refresh",uuid.toString());
            return Jwts.builder()
                    .claim("claim",claims)
                    .signWith(secretKey,SignatureAlgorithm.HS256)
                    .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 1000))
                    .compact();
        }else {

            return Jwts.builder()
                    .setSubject(member.getEmail())
                    .signWith(secretKey,SignatureAlgorithm.HS256)
                    .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 1000))
                    .compact();
        }
    }

    public String createRefreshToken(Member member){
        String refreshToken = createToken(member, REFRESH_TOKEN_EXPIRED_TIME, true);
        saveRefreshToken(member,refreshToken);
        return refreshToken;
    }

    public String createAccessToken(Member member){
      return createToken(member,ACCESS_TOKEN_EXPIRED_TIME,false);
    }

    public void saveRefreshToken(Member member,String refreshToken){
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    public boolean isValidToken(String token){
        try{
            Date expiredDate = jwtParser.parseClaimsJws(token).getBody().getExpiration();
            Date now = new Date();
            return  expiredDate.after(now);
        }catch (ExpiredJwtException e){
            log.error("토큰이 만료되었습니다. - {}",e.getMessage());
        }catch (Exception e){
            log.error("토큰 파싱 오류발생");
            e.printStackTrace();
        }
        return false;
    }

    public Authentication getAuthentication(String token) throws UsernameNotFoundException {
        String memberEmail = jwtParser.parseClaimsJws(token).getBody().getSubject();
        MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(memberEmail);
        return new CustomAuthenticationToken(memberContext.getMember(),null,memberContext.getAuthorities());
    }
}

