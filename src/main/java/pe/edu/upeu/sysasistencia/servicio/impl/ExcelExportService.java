package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysasistencia.modelo.Matricula;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");

    /**
     * Exporta una lista de matrículas a un archivo Excel
     */
    public byte[] exportarMatriculasAExcel(List<Matricula> matriculas) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Matrículas");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle dateTimeStyle = createDateTimeStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);

            // Crear encabezado
            crearEncabezado(sheet, headerStyle);

            // Llenar datos
            int rowNum = 1;
            for (Matricula matricula : matriculas) {
                Row row = sheet.createRow(rowNum++);
                llenarFila(row, matricula, textStyle, dateStyle, dateTimeStyle);
            }

            // Ajustar ancho de columnas
            ajustarAnchoColumnas(sheet);

            // Escribir al output stream
            workbook.write(out);
            log.info("Excel generado exitosamente con {} registros", matriculas.size());

            return out.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar Excel: {}", e.getMessage());
            throw new Exception("Error al generar el archivo Excel: " + e.getMessage());
        }
    }

    /**
     * Crear encabezado del Excel
     */
    private void crearEncabezado(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        String[] columnas = {
                "Modo contrato",
                "Modalidad estudio",
                "Sede",
                "Unidad académica",
                "Programa estudio",
                "Ciclo",
                "Grupo",
                "id_persona",
                "Código estudiante",
                "Estudiante",
                "Documento",
                "Correo",
                "Usuario",
                "Correo Institucional",
                "Celular",
                "Pais",
                "Foto",
                "Religión",
                "Fecha de nacimiento",
                "Fecha de matrícula"
        };

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Llenar una fila con datos de matrícula
     */
    private void llenarFila(Row row, Matricula m, CellStyle textStyle,
                            CellStyle dateStyle, CellStyle dateTimeStyle) {
        int colNum = 0;

        // Modo contrato
        createCell(row, colNum++, m.getModoContrato(), textStyle);

        // Modalidad estudio
        createCell(row, colNum++, m.getModalidadEstudio(), textStyle);

        // Sede
        createCell(row, colNum++, m.getSede() != null ? m.getSede().getNombre() : "", textStyle);

        // Unidad académica (Facultad)
        createCell(row, colNum++, m.getFacultad() != null ? m.getFacultad().getNombre() : "", textStyle);

        // Programa estudio
        createCell(row, colNum++, m.getProgramaEstudio() != null ? m.getProgramaEstudio().getNombre() : "", textStyle);

        // Ciclo
        createCell(row, colNum++, m.getCiclo(), textStyle);

        // Grupo
        createCell(row, colNum++, m.getGrupo(), textStyle);

        // id_persona
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getIdPersona().toString() : "", textStyle);

        // Código estudiante
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getCodigoEstudiante() : "", textStyle);

        // Estudiante (nombre completo)
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getNombreCompleto() : "", textStyle);

        // Documento
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getDocumento() : "", textStyle);

        // Correo
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getCorreo() : "", textStyle);

        // Usuario
        createCell(row, colNum++, m.getPersona() != null && m.getPersona().getUsuario() != null ?
                m.getPersona().getUsuario().getUser() : "", textStyle);

        // Correo Institucional
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getCorreoInstitucional() : "", textStyle);

        // Celular
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getCelular() : "", textStyle);

        // País
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getPais() : "", textStyle);

        // Foto
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getFoto() : "", textStyle);

        // Religión
        createCell(row, colNum++, m.getPersona() != null ? m.getPersona().getReligion() : "", textStyle);

        // Fecha de nacimiento (solo fecha)
        if (m.getPersona() != null && m.getPersona().getFechaNacimiento() != null) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(m.getPersona().getFechaNacimiento().format(DATE_FORMATTER));
            cell.setCellStyle(dateStyle);
        } else {
            createCell(row, colNum++, "", textStyle);
        }

        // Fecha de matrícula (con hora)
        if (m.getFechaMatricula() != null) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(m.getFechaMatricula().format(DATETIME_FORMATTER));
            cell.setCellStyle(dateTimeStyle);
        } else {
            createCell(row, colNum++, "", textStyle);
        }
    }

    /**
     * Crear celda con valor y estilo
     */
    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    /**
     * Estilo para encabezado
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * Estilo para celdas de texto
     */
    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Estilo para fechas (sin hora)
     */
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Estilo para fechas con hora
     */
    private CellStyle createDateTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Ajustar ancho de columnas automáticamente
     */
    private void ajustarAnchoColumnas(Sheet sheet) {
        for (int i = 0; i < 20; i++) {
            sheet.autoSizeColumn(i);
            // Añadir un poco de padding extra
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 1000);
        }
    }
}