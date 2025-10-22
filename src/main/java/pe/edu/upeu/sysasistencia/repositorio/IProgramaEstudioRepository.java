package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.ProgramaEstudio;
import java.util.Optional;

public interface IProgramaEstudioRepository extends ICrudGenericoRepository<ProgramaEstudio, Long>{
    Optional<ProgramaEstudio> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}