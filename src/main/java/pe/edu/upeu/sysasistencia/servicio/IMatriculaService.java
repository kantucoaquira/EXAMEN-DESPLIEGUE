package pe.edu.upeu.sysasistencia.servicio;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.sysasistencia.dtos.ImportFilterDTO;
import pe.edu.upeu.sysasistencia.dtos.ImportResultDTO;
import pe.edu.upeu.sysasistencia.modelo.Matricula;
import pe.edu.upeu.sysasistencia.modelo.TipoPersona;

import java.util.List;

public interface IMatriculaService extends ICrudGenericoService<Matricula, Long>{
    List<Matricula> findByCodigoEstudiante(String codigo);

    List<Matricula> findByFiltros(Long sedeId, Long facultadId, Long programaId, TipoPersona tipoPersona);

    ImportResultDTO importarDesdeExcel(MultipartFile file, ImportFilterDTO filtros) throws Exception;

    byte[] exportarMatriculasAExcel(Long sedeId, Long facultadId, Long programaId, TipoPersona tipoPersona) throws Exception;
}