package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.sysasistencia.dtos.PersonaDTO;
import pe.edu.upeu.sysasistencia.modelo.Persona;

@Mapper(componentModel = "spring")
public interface PersonaMapper extends GenericMapper<PersonaDTO, Persona> {
    @Mapping(source = "usuario.idUsuario", target = "usuarioId")
    PersonaDTO toDTO(Persona persona);

    @Mapping(source = "usuarioId", target = "usuario.idUsuario")
    Persona toEntity(PersonaDTO dto);
}