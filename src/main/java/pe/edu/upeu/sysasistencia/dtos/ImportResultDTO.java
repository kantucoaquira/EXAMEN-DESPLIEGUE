package pe.edu.upeu.sysasistencia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImportResultDTO {
    private int totalRegistros;
    private int exitosos;
    private int fallidos;
    private List<String> errores = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
}