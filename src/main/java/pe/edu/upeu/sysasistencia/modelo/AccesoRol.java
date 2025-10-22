package pe.edu.upeu.sysasistencia.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_acceso_rol")
@IdClass(AccesoRolPK.class)
public class AccesoRol {
    @Id
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Id
    @ManyToOne
    @JoinColumn(name = "acceso_id")
    private Acceso acceso;
}