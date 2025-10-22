package pe.edu.upeu.sysasistencia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysasistencia.modelo.Usuario;
import pe.edu.upeu.sysasistencia.modelo.UsuarioRol;
import pe.edu.upeu.sysasistencia.repositorio.IUsuarioRepository;
import pe.edu.upeu.sysasistencia.repositorio.IUsuarioRolRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final IUsuarioRolRepository repo;
    private final IUsuarioRepository repoU;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repoU.findOneByUser(username).orElse(null);
        List<UsuarioRol> user = repo.findOneByUsuarioUser(username);

        if (user == null || user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        user.forEach(rol -> {
            roles.add(new SimpleGrantedAuthority(rol.getRol().getNombre().name()));
        });

        return new org.springframework.security.core.userdetails.User(u.getUser(), u.getClave(), roles);
    }
}