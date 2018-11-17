package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.JwtAuthenticationResponse;
import com.noisepipe.server.payload.LoginRequest;
import com.noisepipe.server.payload.SignUpRequest;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    String jwt = userService.authenticateUser(loginRequest);

    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userService.checkUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이름입니다."), HttpStatus.BAD_REQUEST
      );
    }
    if (userService.checkEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이메일입니다."), HttpStatus.BAD_REQUEST
      );
    }

    User user = userService.registerUser(signUpRequest);

    // 요청 처리 후 브라우저가 액세스할 url 생성 & location header로 return
    URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/{username}")
            .buildAndExpand(user.getUsername()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "회원가입에 성공했습니다."));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable String userId) {
    Long _userId;
    try {
      _userId = Long.valueOf(userId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    userService.deleteUser(currentUser, _userId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user"));
  }
}
