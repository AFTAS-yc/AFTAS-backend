package com.api.AFTAS.security.User;

import com.api.AFTAS.domains.hunting.Hunting;
import com.api.AFTAS.domains.member.IdentityDocumentType;
import com.api.AFTAS.domains.ranking.Ranking;
import com.api.AFTAS.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
//public class User {
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String fullName;
    protected LocalDate dateOfBirth;
    @Column(unique = true)
    protected String email;
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Role role;
    private LocalDate AccessionDate;
    private String nationality;
    private IdentityDocumentType identityDocument;
    private String identityNumber;
    @OneToMany(mappedBy = "member")
    private List<Hunting> huntings = new ArrayList<>();
    @OneToMany(mappedBy = "id.member")
    private List<Ranking> rankings;
    @OneToMany(mappedBy = "user")
    protected List<Token> tokens;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return true;
    }
}
