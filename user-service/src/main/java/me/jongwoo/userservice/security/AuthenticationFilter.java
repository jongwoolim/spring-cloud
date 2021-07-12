package me.jongwoo.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import me.jongwoo.userservice.dto.AccountDto;
import me.jongwoo.userservice.service.AccountService;
import me.jongwoo.userservice.vo.RequestLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AccountService accountService;
    private Environment env;


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AccountService accountService,
                                Environment env) {
        super(authenticationManager);
        this.accountService = accountService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // InputStream 쓴 이유 RequestLogin이 POST 방식으로 파라미터로 받을 수 없기 때문에
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);


            //사용자가 입력한 정보를 토큰으로 바꾸어 인증처리 요청
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername();
        AccountDto userDetails = accountService.getAccountDetailsByEmail(userName);

        String token = Jwts.builder()
                            .setSubject(userDetails.getUserId())
                            .setExpiration(new Date(System.currentTimeMillis() +
                                    Long.parseLong(env.getProperty("token.expiration_time"))))
                            .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                            .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
