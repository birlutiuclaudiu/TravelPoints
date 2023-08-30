package com.disi.travelpoints.model.entity;


import com.disi.travelpoints.model.enums.AccountStatus;
import com.disi.travelpoints.model.enums.TravelUserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "travel_users")
public class TravelUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotNull
    @Size(min = 1, max = 100)
    private String email;

    @Column(name = "user_password", nullable = false)
    @NotNull
    private String userPassword;

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "role", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TravelUserRole role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountTokenValidation tokensValidation;

    @OneToMany(mappedBy = "travelUser", cascade = CascadeType.ALL)
    private List<Visit> visits;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user", cascade = CascadeType.ALL)
    Set<Wishlist> wishlists;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(this.getRole().name()));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.accountStatus.equals(AccountStatus.VALID);
    }
}
