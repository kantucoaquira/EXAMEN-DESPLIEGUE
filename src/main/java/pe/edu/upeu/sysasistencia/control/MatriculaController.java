package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.sysasistencia.dtos.ImportFilterDTO;
import pe.edu.upeu.sysasistencia.dtos.ImportResultDTO;
import pe.edu.upeu.sysasistencia.dtos.MatriculaDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.MatriculaMapper;
import pe.edu.upeu.sysasistencia.modelo.Matricula;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;
import pe.edu.upeu.sysasistencia.servicio.IMatriculaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/matriculas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MatriculaController {
    private final IMatriculaService matriculaService;
    private final MatriculaMapper matriculaMapper;

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> findAll() {
        List<MatriculaDTO> list = matriculaMapper.toDTOs(matriculaService.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> findById(@PathVariable Long id) {
        Matricula obj = matriculaService.findById(id);
        return ResponseEntity.ok(matriculaMapper.toDTO(obj));
    }

    @GetMapping("/estudiante/{codigo}")
    public ResponseEntity<List<MatriculaDTO>> findByCodigoEstudiante(@PathVariable String codigo) {
        List<MatriculaDTO> list = matriculaMapper.toDTOs(
                matriculaService.findByCodigoEstudiante(codigo)
        );
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para obtener matrículas filtradas
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<MatriculaDTO>> findByFiltros(
            @RequestParam(required = false) Long sedeId,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) Long programaId,
            @RequestParam(required = false) TipoPersona tipoPersona
    ) {
        List<MatriculaDTO> list = matriculaMapper.toDTOs(
                matriculaService.findByFiltros(sedeId, facultadId, programaId, tipoPersona)
        );
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> save(@RequestBody MatriculaDTO dto) {
        Matricula obj = matriculaService.save(matriculaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaMapper.toDTO(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> update(@PathVariable Long id, @RequestBody MatriculaDTO dto) {
        dto.setIdMatricula(id);
        Matricula obj = matriculaService.update(id, matriculaMapper.toEntity(dto));
        return ResponseEntity.ok(matriculaMapper.toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.delete(id));
    }

    /**
     * Endpoint mejorado para importar desde Excel con filtros
     * Si se aplican filtros (sede, facultad, programa), solo se importarán registros que coincidan
     * Si no se aplican filtros, se importarán todos los registros del Excel
     */
    @PostMapping("/importar")
    public ResponseEntity<ImportResultDTO> importarExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long sedeId,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) Long programaId,
            @RequestParam(required = false) TipoPersona tipoPersona
    ) {
        try {
            // Validar archivo
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                throw new RuntimeException("El archivo debe ser un Excel (.xlsx o .xls)");
            }

            // Crear objeto de filtros
            ImportFilterDTO filtros = new ImportFilterDTO();
            filtros.setSedeId(sedeId);
            filtros.setFacultadId(facultadId);
            filtros.setProgramaId(programaId);
            filtros.setTipoPersona(tipoPersona);

            // Importar con filtros
            ImportResultDTO result = matriculaService.importarDesdeExcel(file, filtros);

            // Agregar información sobre los filtros aplicados
            if (filtros.tieneFiltros()) {
                result.getWarnings().add("Se aplicaron filtros en la importación");
                if (sedeId != null) result.getWarnings().add("Filtro Sede ID: " + sedeId);
                if (facultadId != null) result.getWarnings().add("Filtro Facultad ID: " + facultadId);
                if (programaId != null) result.getWarnings().add("Filtro Programa ID: " + programaId);
                if (tipoPersona != null) result.getWarnings().add("Tipo Persona: " + tipoPersona);
            } else {
                result.getWarnings().add("No se aplicaron filtros - se importaron todos los registros válidos");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            ImportResultDTO errorResult = new ImportResultDTO();
            errorResult.setTotalRegistros(0);
            errorResult.setExitosos(0);
            errorResult.setFallidos(0);
            errorResult.getErrores().add("Error general: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarExcel(
            @RequestParam(required = false) Long sedeId,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) Long programaId,
            @RequestParam(required = false) TipoPersona tipoPersona
    ) {
        try {
            byte[] excelBytes = matriculaService.exportarMatriculasAExcel(sedeId, facultadId, programaId, tipoPersona);

            // Generar nombre de archivo con timestamp
            String filename = "Matriculas_" +
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error al exportar Excel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}