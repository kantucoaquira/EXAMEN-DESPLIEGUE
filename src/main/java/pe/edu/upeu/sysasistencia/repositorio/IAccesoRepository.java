package pe.edu.upeu.sysasistencia.repositorio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.sysasistencia.modelo.Acceso;
import java.util.List;
import java.util.Optional;

public interface IAccesoRepository extends ICrudGenericoRepository<Acceso, Long> {
    @Query(value = """
     SELECT a.* FROM upeu_acceso_rol ar
     INNER JOIN upeu_usuario_rol ur ON (ur.rol_id=ar.rol_id)
     INNER JOIN upeu_accesos a ON (a.id_acceso=ar.acceso_id)
     INNER JOIN upeu_usuario u ON (u.id_usuario=ur.usuario_id)
     WHERE u.user=:username
 """, nativeQuery = true)
    List<Acceso> getAccesoByUser(@Param("username") String username);

    Optional<Acceso> findByNombre(String nombre);

    boolean existsByUrl(String url);
}