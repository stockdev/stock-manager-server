package mycode.stockmanager.app.system.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN(Sets.newHashSet(UserPermission.USER_READ,UserPermission.USER_WRITE)),
    MANAGER(Sets.newHashSet(UserPermission.USER_READ)),
    HELPER(Sets.newHashSet(UserPermission.USER_READ));
    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission->new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));

        return permissions;
    }

}
