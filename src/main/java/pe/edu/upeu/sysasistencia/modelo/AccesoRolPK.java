package pe.edu.upeu.sysasistencia.modelo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class AccesoRolPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "acceso_id")
    private Acceso acceso;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}