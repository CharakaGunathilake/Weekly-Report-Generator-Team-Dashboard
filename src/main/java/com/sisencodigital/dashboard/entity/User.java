package com.sisencodigital.dashboard.entity;

import com.sisencodigital.dashboard.enums.UserRole;
import com.sisencodigital.dashboard.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"projects", "reports"})
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false)
    private String password;
    private Instant lastLoginAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Report> reports;
    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.LAZY)
    private Set<Project> projects;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (lastLoginAt == null) return !status.equals(UserStatus.INACTIVE);
        long daysSinceLogin = Duration.between(lastLoginAt, Instant.now()).toDays();
        return daysSinceLogin <= 30 && !UserStatus.INACTIVE.equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(UserStatus.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId() != null && getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
