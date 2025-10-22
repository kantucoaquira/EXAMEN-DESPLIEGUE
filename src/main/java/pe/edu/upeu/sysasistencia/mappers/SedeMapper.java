package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import pe.edu.upeu.sysasistencia.dtos.SedeDTO;
import pe.edu.upeu.sysasistencia.modelo.Sede;

@Mapper(componentModel = "spring")
public interface SedeMapper extends GenericMapper<SedeDTO, Sede> {
}