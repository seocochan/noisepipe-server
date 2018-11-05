package com.noisepipe.server.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal

// @AuthenticationPrincipal 어노테이션을 래핑한 어노테이션 정의
public @interface CurrentUser {
}
