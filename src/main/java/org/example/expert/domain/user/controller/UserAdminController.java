package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.UserAdminService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @Secured(UserRole.Authority.ADMIN)
    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(
            @PathVariable long userId,
            @RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
