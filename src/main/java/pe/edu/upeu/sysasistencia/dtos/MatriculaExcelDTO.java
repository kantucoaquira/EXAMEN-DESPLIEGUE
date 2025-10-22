package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatriculaExcelDTO {
    private String modoContrato;
    private String modalidadEstudio;
    private String sede;
    private String unidadAcademica; // Facultad
    private String programaEstudio;
    private String ciclo;
    private String grupo;
    private String idPersona;
    private String codigoEstudiante;
    private String estudiante; // Nombre completo
    private String documento;
    private String correo;
    private String usuario;
    private String correoInstitucional;
    private String celular;
    private String pais;
    private String foto;
    private String religion;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaMatricula;
}