package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole userRole;
    private final String nickname;

    public AuthUser(Long id, String email, UserRole userRole, String nickname) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+userRole.name()));
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
