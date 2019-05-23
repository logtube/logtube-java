package io.github.logtube.demo;

import io.github.logtube.http.LogtubeHttpFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public FilterRegistrationBean<LogtubeHttpFilter> someFilterRegistration() {
        FilterRegistrationBean<LogtubeHttpFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogtubeHttpFilter());
        registration.addUrlPatterns("*");
        registration.setName("logtube-http");
        registration.setOrder(1);
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
