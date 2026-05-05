package com.Lifelink.HeathCareBridge.security;




import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.secretKey}")
    private String secretKey;
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;
    //adding logger
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request , jwtCookie);
        return cookie.getValue();
    }
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //generating jwtCookies
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwtToken = generateTokenFromUserName(userDetails);
        return ResponseCookie.from(jwtCookie , jwtToken).
                path("/api")
                .maxAge(24*60*60*10).
                httpOnly(false).build();
    }

    // method for generating token form user name
    public String generateTokenFromUserName(UserDetails userDetails){
        String userName = userDetails.getUsername();
        return Jwts.builder().subject(userName).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime() + jwtExpirationMs)).
                signWith(key()).
                compact();
    }
    //getting userName from Token
    public String  UserNameFromJwtToken(String token){
        return Jwts.parser().
                verifyWith((SecretKey) key()).build().
                parseSignedClaims(token).
                getPayload().getSubject();
    }
    //generating key
    /*
    steps involve  :

    1) select algo for key generation
    2) select decoder
    3) put a secret key  in the argument
     */
    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secretKey));
    }
    // validate jwt token
    public boolean validateToken(String authToken){
         /*
         we could potentially find exception here , so i will make use of try and catch block
          */
        try{
            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException e){
            logger.error("Jwt token is malformed : {}"  , e.getMessage());
        }catch(ExpiredJwtException e){
            logger.error("Jwt token is expired : {}" , e.getMessage());
        }catch(UnsupportedJwtException e){
            logger.error("Invalid Jwt token : {} " , e.getMessage());
        }catch(IllegalArgumentException e){
            logger.error("Unsupported Jwt token : {}" , e.getMessage());
        }
        return false;
    }

    //generating clean cookie to override existing cookie
    //this is essential for logging out
    public ResponseCookie getCleanCookie(){
        return ResponseCookie.from(jwtCookie , null).path("/api").build();
    }
}

