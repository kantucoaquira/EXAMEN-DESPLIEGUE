package pe.edu.upeu.sysasistencia.control;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;
import pe.edu.upeu.sysasistencia.security.JwtTokenUtil;
import pe.edu.upeu.sysasistencia.security.JwtUserDetailsService;
import pe.edu.upeu.sysasistencia.servicio.IUsuarioService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class AuthController {
    private final IUsuarioService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody @Valid UsuarioDTO.CredencialesDto credentialsDto,
                                            HttpServletRequest request) {
        UsuarioDTO userDto = userService.login(credentialsDto);
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(credentialsDto.user());
        userDto.setToken(jwtTokenUtil.generateToken(userDetails));
        request.getSession().setAttribute("USER_SESSION", userDto.getUser());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody @Valid UsuarioDTO.UsuarioCrearDto user) {
        UsuarioDTO createdUser = userService.register(user);
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.user());
        createdUser.setToken(jwtTokenUtil.generateToken(userDetails));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getUser())).body(createdUser);
    }

}