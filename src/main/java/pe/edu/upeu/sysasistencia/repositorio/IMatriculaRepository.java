package pe.edu.upeu.sysasistencia.repositorio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.sysasistencia.modelo.Matricula;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;

import java.util.List;
import java.util.Optional;

public interface IMatriculaRepository extends ICrudGenericoRepository<Matricula, Long>{
    Optional<Matricula> findByPersonaIdPersona(Long idPersona);

    @Query("SELECT m FROM Matricula m WHERE m.persona.codigoEstudiante = :codigo")
    List<Matricula> findByCodigoEstudiante(@Param("codigo") String codigo);

    @Query("SELECT m FROM Matricula m WHERE " +
            "(:sedeId IS NULL OR m.sede.idSede = :sedeId) AND " +
            "(:facultadId IS NULL OR m.facultad.idFacultad = :facultadId) AND " +
            "(:programaId IS NULL OR m.programaEstudio.idPrograma = :programaId) AND " +
            "(:tipoPersona IS NULL OR m.persona.tipoPersona = :tipoPersona)")
    List<Matricula> findByFiltros(
            @Param("sedeId") Long sedeId,
            @Param("facultadId") Long facultadId,
            @Param("programaId") Long programaId,
            @Param("tipoPersona") TipoPersona tipoPersona
    );
}