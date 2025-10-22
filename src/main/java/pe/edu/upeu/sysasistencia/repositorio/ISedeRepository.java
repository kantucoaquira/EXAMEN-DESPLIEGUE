package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Sede;
import java.util.Optional;

public interface ISedeRepository extends ICrudGenericoRepository<Sede, Long>{
    Optional<Sede> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}