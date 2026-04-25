package com.Lifelink.HeathCareBridge.AppConfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReqConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
