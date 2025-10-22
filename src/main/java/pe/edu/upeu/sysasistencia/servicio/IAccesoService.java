package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.modelo.Acceso;
import java.util.List;

public interface IAccesoService {
    List<Acceso> getAccesoByUser(String username);
}
