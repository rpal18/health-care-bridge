package com.Lifelink.HeathCareBridge.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
---------------------------------------------------------------------------------------------------------------
This class is responsible for handling the response in user-friendly way ,
that can easily be used by other framework such as react , flutter etc .
If we do not use this class here , then Spring Security sends Html file by-default
and this Html file could not easily be translated by frontend framework (especially in
mobile application ) .
---------------------------------------------------------------------------------------------------------------
 */

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized access : {} "  , authException.getMessage());
        //setting the response type
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final Map<String , Object > map = new HashMap<>();
        map.put("Status" , HttpServletResponse.SC_UNAUTHORIZED);
        map.put("error" ,  "unauthorized error");
        map.put("message" , authException.getMessage());
        map.put("path" , request.getServletPath());

        //the below lines are responsible for mapping the response into Json Response
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream() , map);


    }
}

