package com.noisepipe.server.service;

import com.noisepipe.server.exception.AppException;
import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Role;
import com.noisepipe.server.model.enums.RoleName;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.LoginRequest;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.payload.SignUpRequest;
import com.noisepipe.server.payload.UserProfile;
import com.noisepipe.server.repository.RoleRepository;
import com.noisepipe.server.repository.UserRepository;
import com.noisepipe.server.security.JwtTokenProvider;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public Boolean checkUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public String authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return tokenProvider.generateToken(authentication);
  }

  public User registerUser(SignUpRequest signUpRequest) {
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException("User Role not set."));
    User user = User.builder()
            .username(signUpRequest.getUsername())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .roles(Collections.singleton(userRole))
            .build();

    return userRepository.save(user);
  }

  public void deleteUser(UserPrincipal currentUser, Long userId) {
    if (!userId.equals(currentUser.getId())) {
      throw new BadRequestException("Permission denied");
    }
    userRepository.deleteById(userId);
  }

  public UserProfile getUserProfile(String username) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new UserProfile(user.getId(), user.getUsername(), user.getCreatedAt());
  }

  public PagedResponse<UserProfile> searchUsers(String q, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<User> userPage
            = userRepository.findByUsernameContainingIgnoreCase(q, pageable);

    if (userPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), userPage);
    }
    List<UserProfile> userProfiles = userPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(userProfiles, userPage);
  }
}