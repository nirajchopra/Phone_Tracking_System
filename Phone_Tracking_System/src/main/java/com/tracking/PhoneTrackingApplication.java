package com.tracking;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class PhoneTrackingApplication {
    public static void main(String[] args) {
		SpringApplication.run(PhoneTrackingApplication.class, args);
    }
    
    @Bean
    public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true);
	}
}