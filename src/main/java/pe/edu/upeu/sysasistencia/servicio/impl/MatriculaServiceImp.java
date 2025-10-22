package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.sysasistencia.dtos.ImportFilterDTO;
import pe.edu.upeu.sysasistencia.dtos.ImportResultDTO;
import pe.edu.upeu.sysasistencia.modelo.*;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IMatriculaRepository;
import pe.edu.upeu.sysasistencia.repositorio.IUsuarioRepository;
import pe.edu.upeu.sysasistencia.servicio.*;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MatriculaServiceImp extends CrudGenericoServiceImp<Matricula, Long> implements IMatriculaService {
    private final IMatriculaRepository repo;
    private final IPersonaService personaService;
    private final ISedeService sedeService;
    private final IFacultadService facultadService;
    private final IProgramaEstudioService programaService;
    private final IUsuarioRepository usuarioRepository;
    private final IRolService rolService;
    private final IUsuarioRolService usuarioRolService;
    private final PasswordEncoder passwordEncoder;

    // Formateadores para parsear fechas con hora
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a", Locale.forLanguageTag("es")),
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.forLanguageTag("es")),
            DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm", Locale.forLanguageTag("es")),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es")),
            DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a", Locale.forLanguageTag("es")),
            DateTimeFormatter.ofPattern("dd-MM-yyyy H:mm", Locale.forLanguageTag("es"))
    };

    // Formateadores para parsear solo fechas
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    @Override
    protected ICrudGenericoRepository<Matricula, Long> getRepo() {
        return repo;
    }

    @Override
    public List<Matricula> findByCodigoEstudiante(String codigo) {
        return repo.findByCodigoEstudiante(codigo);
    }

    @Override
    public List<Matricula> findByFiltros(Long sedeId, Long facultadId, Long programaId, TipoPersona tipoPersona) {
        return repo.findByFiltros(sedeId, facultadId, programaId, tipoPersona);
    }

    @Override
    public ImportResultDTO importarDesdeExcel(MultipartFile file, ImportFilterDTO filtros) throws Exception {
        ImportResultDTO result = new ImportResultDTO();
        result.setTotalRegistros(0);
        result.setExitosos(0);
        result.setFallidos(0);

        TipoPersona tipoPersona = filtros.esEstudiante() ? TipoPersona.ESTUDIANTE : TipoPersona.INVITADO;
        if (filtros.getTipoPersona() != null) {
            tipoPersona = filtros.getTipoPersona();
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() <= 1) {
                result.getErrores().add("El archivo Excel está vacío o sin datos");
                return result;
            }
            int totalRows = sheet.getPhysicalNumberOfRows();
            result.setTotalRegistros(totalRows - 1);

            log.info("Iniciando importación con filtros: Sede={}, Facultad={}, Programa={}, TipoPersona={}",
                    filtros.getSedeId(), filtros.getFacultadId(), filtros.getProgramaId(), tipoPersona);

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    procesarFila(row, i + 1, filtros, tipoPersona, result);
                    result.setExitosos(result.getExitosos() + 1);
                } catch (Exception e) {
                    result.setFallidos(result.getFallidos() + 1);
                    result.getErrores().add("Fila " + (i + 1) + ": " + e.getMessage());
                    log.error("Error procesando fila {}: {}", i + 1, e.getMessage());
                }
            }

            log.info("Importación completada. Total: {}, Exitosos: {}, Fallidos: {}",
                    result.getTotalRegistros(), result.getExitosos(), result.getFallidos());

        } catch (Exception e) {
            throw new Exception("Error al procesar el archivo Excel: " + e.getMessage());
        }

        return result;
    }

    private void procesarFila(Row row, int rowNum, ImportFilterDTO filtros, TipoPersona tipoPersona, ImportResultDTO result) throws Exception {
        try {
            // Leer datos del Excel
            String modoContrato = getCellValueAsString(row.getCell(0));
            String modalidadEstudio = getCellValueAsString(row.getCell(1));
            String sedeNombre = getCellValueAsString(row.getCell(2));
            String facultadNombre = getCellValueAsString(row.getCell(3));
            String programaNombre = getCellValueAsString(row.getCell(4));

            // ✅ DETECCIÓN MEJORADA CON VALIDACIÓN DE FILTROS
            boolean tieneDatosAcademicos = (modoContrato != null && !modoContrato.trim().isEmpty()) &&
                    (modalidadEstudio != null && !modalidadEstudio.trim().isEmpty()) &&
                    (sedeNombre != null && !sedeNombre.trim().isEmpty()) &&
                    (programaNombre != null && !programaNombre.trim().isEmpty());

            // Validar coherencia entre datos y filtros
            if (tieneDatosAcademicos) {
                if (filtros.getTipoPersona() != null && filtros.getTipoPersona() != TipoPersona.ESTUDIANTE) {
                    throw new Exception("Registro tiene datos académicos pero filtro especifica " + filtros.getTipoPersona());
                }
                tipoPersona = TipoPersona.ESTUDIANTE;
                log.info("Fila {} detectada como ESTUDIANTE por tener datos académicos completos", rowNum);
            } else {
                if (filtros.getTipoPersona() == TipoPersona.ESTUDIANTE) {
                    throw new Exception("Filtro es ESTUDIANTE pero registro no tiene datos académicos completos");
                }
                if (filtros.getTipoPersona() == null) {
                    tipoPersona = TipoPersona.INVITADO;
                }
            }

            String ciclo = getCellValueAsString(row.getCell(5));
            String grupo = getCellValueAsString(row.getCell(6));
            String idPersonaStr = getCellValueAsString(row.getCell(7));
            String codigoEstudiante = getCellValueAsString(row.getCell(8));
            String nombreCompleto = getCellValueAsString(row.getCell(9));
            String documento = getCellValueAsString(row.getCell(10));

            // ✅ CORREGIDO: Manejar correos vacíos o null
            String correo = getCellValueAsString(row.getCell(11));
            if (correo != null) correo = correo.trim();

            String usuario = getCellValueAsString(row.getCell(12));
            if (usuario != null) usuario = usuario.trim();

            String correoInstitucional = getCellValueAsString(row.getCell(13));
            if (correoInstitucional != null) correoInstitucional = correoInstitucional.trim();

            String celular = getCellValueAsString(row.getCell(14));
            String pais = getCellValueAsString(row.getCell(15));
            String foto = getCellValueAsString(row.getCell(16));
            String religion = getCellValueAsString(row.getCell(17));
            LocalDate fechaNacimiento = parseFechaNacimiento(row.getCell(18));
            LocalDateTime fechaMatriculaConHora = parseFechaMatriculaConHora(row.getCell(19));

            // Validaciones básicas - SOLO documento es obligatorio
            if (documento == null || documento.trim().isEmpty()) {
                throw new Exception("Documento vacío - registro omitido");
            }

            // Validar que nombreCompleto no esté vacío
            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                throw new Exception("Nombre completo vacío - registro omitido");
            }

            // Verificar si la persona ya existe
            Optional<Persona> personaExistente = personaService.findByDocumento(documento);
            if (personaExistente.isPresent()) {
                result.getWarnings().add("Fila " + rowNum + ": Persona con documento " + documento + " ya existe - registro omitido");
                throw new Exception("Documento duplicado");
            }

            // Verificar entidades relacionadas (solo para estudiantes)
            Sede sede = null;
            Facultad facultad = null;
            ProgramaEstudio programa = null;

            if (tipoPersona == TipoPersona.ESTUDIANTE) {
                sede = obtenerOValidarSede(sedeNombre, filtros);
                if (sede == null) throw new Exception("Sede no coincide con filtros");

                facultad = obtenerOValidarFacultad(facultadNombre, filtros);
                if (facultad == null) throw new Exception("Facultad no coincide con filtros");

                programa = obtenerOValidarPrograma(programaNombre, facultad, filtros);
                if (programa == null) throw new Exception("Programa no coincide con filtros");
            }

            // ✅ Crear usuario automáticamente (ahora acepta correos null/vacíos)
            Usuario usuarioCreado = crearUsuario(correo, documento, usuario);

            // Crear Persona y asignar usuario
            Persona persona = new Persona();
            persona.setCodigoEstudiante(codigoEstudiante);
            persona.setNombreCompleto(nombreCompleto);
            persona.setDocumento(documento);
            persona.setCorreo(correo);  // Puede ser null
            persona.setCorreoInstitucional(correoInstitucional);  // Puede ser null
            persona.setCelular(celular);
            persona.setPais(pais);
            persona.setFoto(foto);
            persona.setReligion(religion);
            persona.setFechaNacimiento(fechaNacimiento);
            persona.setTipoPersona(tipoPersona);
            persona.setUsuario(usuarioCreado);
            persona = personaService.save(persona);

            // ✅ CREAR MATRÍCULA SOLO PARA ESTUDIANTES
            if (tipoPersona == TipoPersona.ESTUDIANTE) {
                Matricula matricula = new Matricula();
                matricula.setPersona(persona);
                matricula.setSede(sede);
                matricula.setFacultad(facultad);
                matricula.setProgramaEstudio(programa);
                matricula.setModoContrato(modoContrato);
                matricula.setModalidadEstudio(modalidadEstudio);
                matricula.setCiclo(ciclo);
                matricula.setGrupo(grupo);
                matricula.setFechaMatricula(fechaMatriculaConHora != null ?
                        fechaMatriculaConHora : LocalDateTime.now());
                matricula.setEstado("ACTIVO");
                repo.save(matricula);

                log.info("✅ Fila {} procesada como ESTUDIANTE: {} - Usuario: {} - Programa: {}",
                        rowNum, documento, usuarioCreado.getUser(), programaNombre);
            } else {
                log.info("✅ Fila {} procesada como INVITADO: {} - Usuario: {}",
                        rowNum, documento, usuarioCreado.getUser());
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * ✅ NUEVO: Crear usuario automáticamente
     * Username: correo o usuario del Excel
     * Password: documento (encriptado)
     * Rol: INTEGRANTE por defecto
     */
    private Usuario crearUsuario(String correo, String documento, String usuarioExcel) throws Exception {
        // Validar que al menos tengamos documento
        if (documento == null || documento.trim().isEmpty()) {
            throw new Exception("No se puede crear usuario sin documento");
        }

        // Determinar el username con prioridad: usuarioExcel > correo > documento
        String username = null;

        // Opción 1: Usar usuarioExcel si existe
        if (usuarioExcel != null && !usuarioExcel.trim().isEmpty()) {
            username = usuarioExcel.trim();
        }
        // Opción 2: Usar correo si existe
        else if (correo != null && !correo.trim().isEmpty()) {
            username = correo.trim();
        }
        // Opción 3: Usar documento como último recurso
        else {
            username = documento.trim();
        }

        log.info("Creando usuario con username: {}", username);

        // Verificar si el usuario ya existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findOneByUser(username);
        if (usuarioExistente.isPresent()) {
            log.warn("Usuario {} ya existe, reutilizando", username);
            return usuarioExistente.get();
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUser(username);
        nuevoUsuario.setClave(passwordEncoder.encode(documento)); // Password = documento
        nuevoUsuario.setEstado("ACTIVO");
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        // Asignar rol INTEGRANTE por defecto
        Rol rolIntegrante = rolService.getByNombre(Rol.RolNombre.INTEGRANTE)
                .orElseThrow(() -> new Exception("Rol INTEGRANTE no encontrado"));

        UsuarioRol usuarioRol = UsuarioRol.builder()
                .usuario(nuevoUsuario)
                .rol(rolIntegrante)
                .build();
        usuarioRolService.save(usuarioRol);

        log.info("✅ Usuario creado: {} con contraseña: {}", username, documento);
        return nuevoUsuario;
    }

    /**
     * ✅ CORREGIDO: Parsear fecha de nacimiento con múltiples formatos
     */
    private LocalDate parseFechaNacimiento(Cell cell) {
        if (cell == null) return null;

        try {
            // Si es una celda con formato de fecha de Excel
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            // Si es texto, intentar parsear con diferentes formatos
            if (cell.getCellType() == CellType.STRING) {
                String dateStr = cell.getStringCellValue().trim();

                for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                    try {
                        return LocalDate.parse(dateStr, formatter);
                    } catch (DateTimeParseException e) {
                        // Intentar con el siguiente formato
                    }
                }

                log.warn("No se pudo parsear la fecha de nacimiento: {}", dateStr);
            }
        } catch (Exception e) {
            log.error("Error parseando fecha de nacimiento: {}", e.getMessage());
        }

        return null;
    }

    /**
     * ✅ NUEVO: Parsear fecha de matrícula CON HORA
     * Formatos soportados:
     * - "07/08/2025 2:15 p. m."
     * - "07/08/2025 14:15"
     * - "07/08/2025" (solo fecha)
     */
    private LocalDateTime parseFechaMatriculaConHora(Cell cell) {
        if (cell == null) return LocalDateTime.now();

        try {
            // Si es una celda con formato de fecha/hora de Excel
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }

            // Si es texto, intentar parsear con diferentes formatos
            if (cell.getCellType() == CellType.STRING) {
                String dateTimeStr = cell.getStringCellValue().trim();

                // Normalizar el texto (quitar espacios múltiples, estandarizar am/pm)
                dateTimeStr = dateTimeStr.replaceAll("\\s+", " ");
                dateTimeStr = dateTimeStr.replace("p. m.", "PM")
                        .replace("a. m.", "AM")
                        .replace("p.m.", "PM")
                        .replace("a.m.", "AM");

                // Intentar parsear con formatos de fecha+hora
                for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
                    try {
                        return LocalDateTime.parse(dateTimeStr, formatter);
                    } catch (DateTimeParseException e) {
                        // Intentar con el siguiente formato
                    }
                }

                // Si no tiene hora, intentar parsear solo la fecha
                for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                    try {
                        LocalDate date = LocalDate.parse(dateTimeStr, formatter);
                        return date.atStartOfDay(); // Convertir a LocalDateTime a las 00:00
                    } catch (DateTimeParseException e) {
                        // Intentar con el siguiente formato
                    }
                }

                log.warn("No se pudo parsear la fecha de matrícula: {}", dateTimeStr);
            }
        } catch (Exception e) {
            log.error("Error parseando fecha de matrícula: {}", e.getMessage());
        }

        return LocalDateTime.now();
    }

    private Sede obtenerOValidarSede(String sedeNombre, ImportFilterDTO filtros) {
        if (filtros.getSedeId() != null) {
            Sede sedeFiltro = sedeService.findById(filtros.getSedeId());
            if (!sedeFiltro.getNombre().equalsIgnoreCase(sedeNombre)) {
                log.warn("Sede '{}' no coincide con filtro '{}'", sedeNombre, sedeFiltro.getNombre());
                return null;
            }
            return sedeFiltro;
        }

        return sedeService.findByNombre(sedeNombre)
                .orElseGet(() -> {
                    Sede nuevaSede = new Sede();
                    nuevaSede.setNombre(sedeNombre);
                    nuevaSede.setDescripcion("Creada automáticamente desde importación");
                    return sedeService.save(nuevaSede);
                });
    }

    private Facultad obtenerOValidarFacultad(String facultadNombre, ImportFilterDTO filtros) {
        if (filtros.getFacultadId() != null) {
            Facultad facultadFiltro = facultadService.findById(filtros.getFacultadId());
            if (!facultadFiltro.getNombre().equalsIgnoreCase(facultadNombre)) {
                log.warn("Facultad '{}' no coincide con filtro '{}'", facultadNombre, facultadFiltro.getNombre());
                return null;
            }
            return facultadFiltro;
        }

        return facultadService.findByNombre(facultadNombre)
                .orElseGet(() -> {
                    Facultad nuevaFacultad = new Facultad();
                    nuevaFacultad.setNombre(facultadNombre);
                    nuevaFacultad.setDescripcion("Creada automáticamente desde importación");
                    return facultadService.save(nuevaFacultad);
                });
    }

    private ProgramaEstudio obtenerOValidarPrograma(String programaNombre, Facultad facultad, ImportFilterDTO filtros) {
        if (filtros.getProgramaId() != null) {
            ProgramaEstudio programaFiltro = programaService.findById(filtros.getProgramaId());
            if (!programaFiltro.getNombre().equalsIgnoreCase(programaNombre)) {
                log.warn("Programa '{}' no coincide con filtro '{}'", programaNombre, programaFiltro.getNombre());
                return null;
            }
            return programaFiltro;
        }

        return programaService.findByNombre(programaNombre)
                .orElseGet(() -> {
                    ProgramaEstudio nuevoPrograma = new ProgramaEstudio();
                    nuevoPrograma.setNombre(programaNombre);
                    nuevoPrograma.setFacultad(facultad);
                    nuevoPrograma.setDescripcion("Creado automáticamente desde importación");
                    return programaService.save(nuevoPrograma);
                });
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public byte[] exportarMatriculasAExcel(Long sedeId, Long facultadId, Long programaId, TipoPersona tipoPersona) throws Exception {
        // Obtener matrículas filtradas
        List<Matricula> matriculas = findByFiltros(sedeId, facultadId, programaId, tipoPersona);

        if (matriculas.isEmpty()) {
            throw new Exception("No hay registros para exportar con los filtros aplicados");
        }

        log.info("Exportando {} matrículas a Excel", matriculas.size());
        return excelExportService.exportarMatriculasAExcel(matriculas);
    }
}