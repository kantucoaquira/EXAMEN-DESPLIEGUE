package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonaDTO {
    private Long idPersona;
    private String codigoEstudiante;
    private String nombreCompleto;
    private String documento;
    private String correo;
    private String correoInstitucional;
    private String celular;
    private String pais;
    private String foto;
    private String religion;
    private LocalDate fechaNacimiento;
    private TipoPersona tipoPersona;
    private Long usuarioId;
}