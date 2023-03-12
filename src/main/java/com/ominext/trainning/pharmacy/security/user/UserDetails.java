package com.ominext.trainning.pharmacy.security.user;

import com.ominext.trainning.pharmacy.security.SimpleUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * AccountDetails
 */
@Data
@NoArgsConstructor
public class UserDetails implements SimpleUserDetails, CredentialsContainer {

    // Id
    private Long id;
    // Password
    private String password;
    // Login for labsyn
    private String username;
    // Authorities
    private Set<GrantedAuthority> authorities;
    // Account non expired
    private boolean accountNonExpired;
    // Account non locked
    private boolean accountNonLocked;
    // Credentials non expired
    private boolean credentialsNonExpired;
    // Enabled
    private boolean enabled;

    private String email;

    public UserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, String email) {
        this(id, username, password, authorities, true, true, true, true, email);
    }

    public UserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities,
                       boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, String email) {

        if (id == null || StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.email = email;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {

        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
