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


    /*
    --------------------------------------------------------------------------------------------------------------------

  This method intercepts every incoming request.
  It checks for a JWT token, validates it, and sets the user authentication
  before the request reaches the controller.

    --------------------------------------------------------------------------------------------------------------------
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for uri : {} " , request.getRequestURI());
        try {

            String token = parseJwt(request);
            //need to validate the token first
            if(token!=null && jwtUtils.validateToken(token)){
                String userName = jwtUtils.UserNameFromJwtToken(token);
                UserDetails userDetail = userDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetail , null , userDetail.getAuthorities());
                /*
                -------------------------------------------------------------------------------------------------------------
                 usernamePasswordAuthenticationToken => Authentication object
                 this below line is  for attaching request detail to authentication object . to do that I am making
                 use of WebAuthenticationDetailsSource class and buildDetails method.
                -------------------------------------------------------------------------------------------------------------
                */
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               /*
               --------------------------------------------------------------------------------------------------------------
                The below line is responsible for setting authentication object in Security context .
               --------------------------------------------------------------------------------------------------------------
                */

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                logger.debug(" Authenticated roles are : {} " , userDetail.getAuthorities());
            }

        }catch(Exception e){
            logger.error("Cannot set Authentication  : {} " , e.getMessage());
        }

        /*
        --------------------------------------------------------------------------------------------------------------------
        Continue the filter chain. Pass the request to the next filter (or the Controller).
        without this line , despite  user being  verified  he will see the blank screen
        because request will not be hand over to controller .
        --------------------------------------------------------------------------------------------------------------------
         */
        filterChain.doFilter(request , response);
    }

    private String parseJwt(HttpServletRequest request){
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        logger.debug("AuthTokenFilter.java : {}" , jwtToken);
        return jwtToken;
    }
}
