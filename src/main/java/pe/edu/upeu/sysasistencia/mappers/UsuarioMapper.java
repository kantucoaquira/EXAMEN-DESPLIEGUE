package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;
import pe.edu.upeu.sysasistencia.modelo.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends GenericMapper<UsuarioDTO, Usuario> {

    @Mapping(target = "token", ignore = true)
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "persona", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "persona", ignore = true)
    Usuario toEntityFromCADTO(UsuarioDTO.UsuarioCrearDto usuarioCrearDto);
}