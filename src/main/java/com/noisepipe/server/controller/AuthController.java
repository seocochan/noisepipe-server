package com.noisepipe.server.controller;

import com.noisepipe.server.exception.AppException;
import com.noisepipe.server.model.Role;
import com.noisepipe.server.model.RoleName;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.JwtAuthenticationResponse;
import com.noisepipe.server.payload.LoginRequest;
import com.noisepipe.server.payload.SignUpRequest;
import com.noisepipe.server.repository.RoleRepository;
import com.noisepipe.server.repository.UserRepository;
import com.noisepipe.server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  JwtTokenProvider tokenProvider;

  /*
  FIXME:
  signup에서 username, email 에러 발생시 exception.BadRequestException 반환
  or AuthController에 @ControllerAdvice, GlobalExceptionhandler 설정하기
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
            )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이름입니다."), HttpStatus.BAD_REQUEST
      );
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이메일입니다."), HttpStatus.BAD_REQUEST
      );
    }

    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
            signUpRequest.getEmail(), signUpRequest.getPassword());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException("User Role not set."));
    user.setRoles(Collections.singleton(userRole));
    User result = userRepository.save(user);

    // 요청 처리 후 브라우저가 액세스할 url 생성 & location header로 return
    URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/{username}")
            .buildAndExpand(result.getUsername()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "회원가입에 성공했습니다."));
  }
}
