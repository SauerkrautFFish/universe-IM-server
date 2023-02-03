package edu.yjzxc.universeimserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//允许域名
        //corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);可以放一个域名列表List<String> allowedOriginPatterns
        corsConfiguration.addAllowedHeader("*");//允许任何头
        corsConfiguration.addAllowedMethod("*");//允许任何方法

        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());

        return new CorsFilter(source);
    }
}
