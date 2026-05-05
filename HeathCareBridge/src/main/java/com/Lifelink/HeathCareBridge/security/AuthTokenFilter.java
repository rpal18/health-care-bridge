package com.Lifelink.HeathCareBridge.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger  = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for uri : {} " , request.getRequestURI());
        try {

            String token = parseJwt(request);

            if(token!=null && jwtUtils.validateToken(token)){
                String userName = jwtUtils.UserNameFromJwtToken(token);
                UserDetails userDetail = userDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetail , null , userDetail.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                logger.debug(" Authenticated roles are : {} " , userDetail.getAuthorities());
            }

        }catch(Exception e){
            logger.error("Cannot set Authentication  : {} " , e.getMessage());
        }
        filterChain.doFilter(request , response);
    }

//    private String parseJwt(HttpServletRequest request){
//        String jwtToken = jwtUtils.getJwtFromCookies(request);
//        logger.debug("AuthTokenFilter.java : {}" , jwtToken);
//        return jwtToken;
//    }

    private String parseJwt(HttpServletRequest request){
        String jwtFromCookies = jwtUtils.getJwtFromCookies(request);
        if(jwtFromCookies!=null){
            return jwtFromCookies;
        }
        String jwtFromHeader = jwtUtils.getJwtFromHeader(request);
        if(jwtFromHeader!=null){
            return jwtFromHeader;
        }
        return null;
    }
}
