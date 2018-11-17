package com.noisepipe.server.controller;

import com.noisepipe.server.payload.*;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());

    return userSummary;
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam String username) {
    Boolean isAvailable = !userService.checkUsername(username);

    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(@RequestParam String email) {
    Boolean isAvailable = !userService.checkEmail(email);

    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/{username}")
  public UserProfile getUserProfile(@PathVariable String username) {
    return userService.getUserProfile(username);
  }
}