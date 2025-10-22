package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import pe.edu.upeu.sysasistencia.dtos.AccesoDTO;
import pe.edu.upeu.sysasistencia.modelo.Acceso;

@Mapper(componentModel = "spring")
public interface AccesoMapper extends GenericMapper<AccesoDTO, Acceso> {
}