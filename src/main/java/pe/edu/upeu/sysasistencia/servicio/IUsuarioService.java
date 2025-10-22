package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;
import pe.edu.upeu.sysasistencia.modelo.Usuario;

public interface IUsuarioService extends ICrudGenericoService<Usuario, Long>{
    UsuarioDTO login(UsuarioDTO.CredencialesDto credentialsDto);
    UsuarioDTO register(UsuarioDTO.UsuarioCrearDto userDto);
}