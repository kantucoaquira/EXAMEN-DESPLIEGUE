package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;
import pe.edu.upeu.sysasistencia.excepciones.ModelNotFoundException;
import pe.edu.upeu.sysasistencia.mappers.UsuarioMapper;
import pe.edu.upeu.sysasistencia.modelo.Rol;
import pe.edu.upeu.sysasistencia.modelo.Usuario;
import pe.edu.upeu.sysasistencia.modelo.UsuarioRol;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IUsuarioRepository;
import pe.edu.upeu.sysasistencia.servicio.IRolService;
import pe.edu.upeu.sysasistencia.servicio.IUsuarioRolService;
import pe.edu.upeu.sysasistencia.servicio.IUsuarioService;
import java.nio.CharBuffer;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImp extends CrudGenericoServiceImp<Usuario, Long> implements IUsuarioService {
    private final IUsuarioRepository repo;
    private final IRolService rolService;
    private final IUsuarioRolService iurService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper userMapper;

    @Override
    protected ICrudGenericoRepository<Usuario, Long> getRepo() {
        return repo;
    }

    @Override
    public UsuarioDTO login(UsuarioDTO.CredencialesDto credentialsDto) {
        Usuario user = repo.findOneByUser(credentialsDto.user())
                .orElseThrow(() -> new ModelNotFoundException("Usuario desconocido", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.clave()), user.getClave())) {
            return userMapper.toDTO(user);
        }

        throw new ModelNotFoundException("Contraseña inválida", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UsuarioDTO register(UsuarioDTO.UsuarioCrearDto userDto) {
        Optional<Usuario> optionalUser = repo.findOneByUser(userDto.user());
        if (optionalUser.isPresent()) {
            throw new ModelNotFoundException("El usuario ya existe", HttpStatus.BAD_REQUEST);
        }

        Usuario user = userMapper.toEntityFromCADTO(userDto);
        user.setClave(passwordEncoder.encode(CharBuffer.wrap(userDto.clave())));
        Usuario savedUser = repo.save(user);

        Rol rol = obtenerRolPorNombre(userDto.rol());

        if (rol == null) {
            throw new ModelNotFoundException("Rol no encontrado: " + userDto.rol(), HttpStatus.BAD_REQUEST);
        }

        iurService.save(UsuarioRol.builder()
                .usuario(savedUser)
                .rol(rol)
                .build());

        return userMapper.toDTO(savedUser);
    }

    private Rol obtenerRolPorNombre(String rolNombre) {
        return switch (rolNombre.toUpperCase()) {
            case "SUPERADMIN" -> rolService.getByNombre(Rol.RolNombre.SUPERADMIN).orElse(null);
            case "ADMIN" -> rolService.getByNombre(Rol.RolNombre.ADMIN).orElse(null);
            case "LIDER" -> rolService.getByNombre(Rol.RolNombre.LIDER).orElse(null);
            case "INTEGRANTE" -> rolService.getByNombre(Rol.RolNombre.INTEGRANTE).orElse(null);
            default -> null;
        };
    }
}