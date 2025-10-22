package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgramaEstudioDTO {
    private Long idPrograma;
    private String nombre;
    private Long facultadId;
    private String facultadNombre;
    private String descripcion;
}