package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImportFilterDTO {
    private Long sedeId;
    private Long facultadId;
    private Long programaId;
    private TipoPersona tipoPersona;

    // Método para verificar si hay filtros activos
    public boolean tieneFiltros() {
        return sedeId != null || facultadId != null || programaId != null || tipoPersona != null;
    }

    // Método para verificar si es estudiante (tiene los 3 filtros principales)
    public boolean esEstudiante() {
        return sedeId != null && facultadId != null && programaId != null;
    }
}