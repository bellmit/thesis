package platform.util.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import platform.model.DPUser;
import platform.persistence.DPUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static platform.util.security.SecurityConstants.*;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private platform.model.DPUser DPUser;
    private platform.persistence.DPUserRepository DPUserRepository;

    public JWTAuthFilter(AuthenticationManager authenticationManager, DPUserRepository DPUserRepository) {
        this.authenticationManager = authenticationManager;
        this.DPUserRepository = DPUserRepository;
    }

    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
             DPUser = new ObjectMapper()
                    .readValue(req.getInputStream(), DPUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            DPUser.getUserName(),
                           DPUser.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
       DPUser DPUser1;
       DPUser1 = DPUserRepository.findByUserName(DPUser.getUserName());
        res.addHeader(HEADER_STRING2, String.valueOf(DPUser1.getId()));
    }
}