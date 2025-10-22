package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatriculaDTO {
    private Long idMatricula;
    private Long personaId;
    private String nombreCompleto;
    private String codigoEstudiante;
    private String documento;
    private TipoPersona tipoPersona;
    private Long sedeId;
    private String sedeName;
    private Long facultadId;
    private String facultadName;
    private Long programaId;
    private String programaName;
    private String modoContrato;
    private String modalidadEstudio;
    private String ciclo;
    private String grupo;
    private LocalDateTime fechaMatricula;
    private String estado;
}