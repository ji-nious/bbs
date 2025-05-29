package com.kh.bbs.web.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final LoginCheckInterceptor loginCheckInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginCheckInterceptor)
        .order(1)
        .addPathPatterns("/boards/**","/comments/**","/comments/**", "/mypage/**") // 로그인 필수 경로
        .excludePathPatterns("/", "/login", "/logout", "/api/boards","/members/join","/css/**", "/js/**", "/images/**"); // 허용 예외
  }
}
