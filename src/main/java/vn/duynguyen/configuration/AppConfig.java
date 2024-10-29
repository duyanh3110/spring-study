package vn.duynguyen.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 1st
//@Configuration
//public class AppConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:5173")
//                .allowedMethods("GET, POST, PUT, DELETE, PATCH")
//                .allowedHeaders("*");
//    }
//}

// 2nd
//@Configuration
//public class AppConfig {
//    @Bean
//    public WebMvcConfigurer corsFilter() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173");
//            }
//        };
//    }
//}

// 3rd
//@Configuration
//public class AppConfig {
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();;
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:5173");
//        config.setAllowedMethods(allowedMethods);
//        config.addAllowedHeader("*");
//        source.registerCorsConfiguration("/user/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }
//}

// 4th
@Component
public class AppConfig extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        filterChain.doFilter(request, response);
    }
}
