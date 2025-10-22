package pe.edu.upeu.sysasistencia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.sysasistencia.dtos.MatriculaDTO;
import pe.edu.upeu.sysasistencia.modelo.Matricula;

@Mapper(componentModel = "spring")
public interface MatriculaMapper extends GenericMapper<MatriculaDTO, Matricula> {
    @Mapping(source = "persona.idPersona", target = "personaId")
    @Mapping(source = "persona.nombreCompleto", target = "nombreCompleto")
    @Mapping(source = "persona.codigoEstudiante", target = "codigoEstudiante")
    @Mapping(source = "persona.documento", target = "documento")
    @Mapping(source = "persona.tipoPersona", target = "tipoPersona")
    @Mapping(source = "sede.idSede", target = "sedeId")
    @Mapping(source = "sede.nombre", target = "sedeName")
    @Mapping(source = "facultad.idFacultad", target = "facultadId")
    @Mapping(source = "facultad.nombre", target = "facultadName")
    @Mapping(source = "programaEstudio.idPrograma", target = "programaId")
    @Mapping(source = "programaEstudio.nombre", target = "programaName")
    MatriculaDTO toDTO(Matricula matricula);

    @Mapping(source = "personaId", target = "persona.idPersona")
    @Mapping(source = "sedeId", target = "sede.idSede")
    @Mapping(source = "facultadId", target = "facultad.idFacultad")
    @Mapping(source = "programaId", target = "programaEstudio.idPrograma")
    @Mapping(target = "persona.nombreCompleto", ignore = true)
    @Mapping(target = "persona.codigoEstudiante", ignore = true)
    @Mapping(target = "persona.documento", ignore = true)
    @Mapping(target = "persona.correo", ignore = true)
    @Mapping(target = "persona.correoInstitucional", ignore = true)
    @Mapping(target = "persona.celular", ignore = true)
    @Mapping(target = "persona.pais", ignore = true)
    @Mapping(target = "persona.foto", ignore = true)
    @Mapping(target = "persona.religion", ignore = true)
    @Mapping(target = "persona.fechaNacimiento", ignore = true)
    @Mapping(target = "persona.tipoPersona", ignore = true)
    @Mapping(target = "persona.usuario", ignore = true)
    @Mapping(target = "sede.nombre", ignore = true)
    @Mapping(target = "sede.descripcion", ignore = true)
    @Mapping(target = "facultad.nombre", ignore = true)
    @Mapping(target = "facultad.descripcion", ignore = true)
    @Mapping(target = "programaEstudio.nombre", ignore = true)
    @Mapping(target = "programaEstudio.facultad", ignore = true)
    @Mapping(target = "programaEstudio.descripcion", ignore = true)
    Matricula toEntity(MatriculaDTO dto);
}