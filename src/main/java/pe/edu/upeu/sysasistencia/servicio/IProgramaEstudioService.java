package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.modelo.ProgramaEstudio;
import java.util.Optional;

public interface IProgramaEstudioService extends ICrudGenericoService<ProgramaEstudio, Long>{
    Optional<ProgramaEstudio> findByNombre(String nombre);
}