package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import pe.edu.upeu.sysasistencia.dtos.FacultadDTO;
import pe.edu.upeu.sysasistencia.modelo.Facultad;

@Mapper(componentModel = "spring")
public interface FacultadMapper extends GenericMapper<FacultadDTO, Facultad> {
}