package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Acceso;
import pe.edu.upeu.sysasistencia.modelo.AccesoRol;
import pe.edu.upeu.sysasistencia.modelo.AccesoRolPK;
import pe.edu.upeu.sysasistencia.modelo.Rol;

public interface IAccesoRolRepository extends ICrudGenericoRepository<AccesoRol, AccesoRolPK> {
    boolean existsByRolAndAcceso(Rol rol, Acceso acceso);
}