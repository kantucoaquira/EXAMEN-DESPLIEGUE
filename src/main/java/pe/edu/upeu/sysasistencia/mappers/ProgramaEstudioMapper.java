package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.sysasistencia.dtos.ProgramaEstudioDTO;
import pe.edu.upeu.sysasistencia.modelo.ProgramaEstudio;

@Mapper(componentModel = "spring")
public interface ProgramaEstudioMapper extends GenericMapper<ProgramaEstudioDTO, ProgramaEstudio> {
    @Mapping(source = "facultad.idFacultad", target = "facultadId")
    @Mapping(source = "facultad.nombre", target = "facultadNombre")
    ProgramaEstudioDTO toDTO(ProgramaEstudio programa);

    @Mapping(source = "facultadId", target = "facultad.idFacultad")
    ProgramaEstudio toEntity(ProgramaEstudioDTO dto);
}