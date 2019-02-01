package com.noisepipe.server.controller;

import com.noisepipe.server.payload.*;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.UserService;
import com.noisepipe.server.utils.AppConstants;
import com.noisepipe.server.utils.ModelMapper;
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
    return ModelMapper.mapToSummary(currentUser.toUser());
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam String username) {
    Boolean isAvailable = !userService.checkUsername(username);

    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/{username}")
  public UserProfile getUserProfile(@PathVariable String username) {
    return userService.getUserProfile(username);
  }

  @GetMapping
  public PagedResponse<UserProfile> searchUsers(
          @RequestParam String q,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return userService.searchUsers(q, page, size);
  }
}