package pe.edu.upeu.sysasistencia.configuracion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.*;
import pe.edu.upeu.sysasistencia.repositorio.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IRolRepository rolRepository;
    private final IAccesoRepository accesoRepository;
    private final IAccesoRolRepository accesoRolRepository;
    private final ISedeRepository sedeRepository;
    private final IFacultadRepository facultadRepository;
    private final IProgramaEstudioRepository programaEstudioRepository;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("Iniciando carga de datos predeterminados...");

            // Crear datos en el orden correcto
            crearSedes();
            crearFacultades();
            crearProgramasEstudio();
            crearRoles();
            crearAccesos();
            asignarAccesosARoles();

            log.info("Carga de datos completada exitosamente");
        } catch (Exception e) {
            log.error("Error al inicializar datos", e);
        }
    }

    private void crearSedes() {
        try {
            String nombreSede = "Filial Juliaca";
            if (!sedeRepository.existsByNombre(nombreSede)) {
                Sede sede = Sede.builder()
                        .nombre(nombreSede)
                        .descripcion("Filial Juliaca")
                        .build();
                Sede sedeGuardada = sedeRepository.save(sede);
                log.info("Sede creada: {} con ID: {}", sedeGuardada.getNombre(), sedeGuardada.getIdSede());
            } else {
                log.debug("La sede '{}' ya existe", nombreSede);
            }
        } catch (Exception e) {
            log.error("Error al crear sede", e);
        }
    }

    private void crearFacultades() {
        try {
            String nombreFacultad = "Facultad de Ingeniería y Arquitectura";
            if (!facultadRepository.existsByNombre(nombreFacultad)) {
                Facultad facultad = Facultad.builder()
                        .nombre(nombreFacultad)
                        .descripcion("Facultad de Ingeniería y Arquitectura")
                        .build();
                Facultad facultadGuardada = facultadRepository.save(facultad);
                log.info("Facultad creada: {} con ID: {}", facultadGuardada.getNombre(), facultadGuardada.getIdFacultad());
            } else {
                log.debug("La facultad '{}' ya existe", nombreFacultad);
            }
        } catch (Exception e) {
            log.error("Error al crear facultad", e);
        }
    }

    private void crearProgramasEstudio() {
        try {
            String nombrePrograma = "EP Ingeniería de Sistemas";
            String nombreFacultad = "Facultad de Ingeniería y Arquitectura";

            if (!programaEstudioRepository.existsByNombre(nombrePrograma)) {
                // Obtener la facultad por nombre
                Facultad facultad = facultadRepository.findByNombre(nombreFacultad)
                        .orElseThrow(() -> new RuntimeException("Facultad no encontrada: " + nombreFacultad));

                ProgramaEstudio programa = ProgramaEstudio.builder()
                        .nombre(nombrePrograma)
                        .descripcion("EP Ingeniería de Sistemas")
                        .facultad(facultad)
                        .build();
                ProgramaEstudio programaGuardado = programaEstudioRepository.save(programa);
                log.info("Programa de estudio creado: {} con ID: {}", programaGuardado.getNombre(), programaGuardado.getIdPrograma());
            } else {
                log.debug("El programa de estudio '{}' ya existe", nombrePrograma);
            }
        } catch (Exception e) {
            log.error("Error al crear programa de estudio", e);
        }
    }

    private void crearRoles() {
        Arrays.stream(Rol.RolNombre.values()).forEach(rolNombre -> {
            Optional<Rol> rolExistente = rolRepository.findByNombre(rolNombre);

            if (rolExistente.isEmpty()) {
                Rol nuevoRol = Rol.builder()
                        .nombre(rolNombre)
                        .descripcion(obtenerDescripcionRol(rolNombre))
                        .build();
                rolRepository.save(nuevoRol);
                log.info("Rol creado: {}", rolNombre);
            } else {
                log.debug("Rol ya existe: {}", rolNombre);
            }
        });
    }

    private String obtenerDescripcionRol(Rol.RolNombre rolNombre) {
        return switch (rolNombre) {
            case SUPERADMIN -> "Super Administrador - Acceso total al sistema";
            case ADMIN -> "Administrador - Gestión de matrículas, sedes, facultades y programas";
            case LIDER -> "Líder - Acceso a dashboard de líder";
            case INTEGRANTE -> "Integrante - Acceso a dashboard de integrante";
        };
    }

    private void crearAccesos() {
        List<Acceso> accesos = Arrays.asList(
                // Accesos SUPERADMIN
                crearAcceso("Usuarios", "/usuarios", "fa-users"),
                crearAcceso("Roles", "/roles", "fa-user-shield"),
                crearAcceso("Configuración", "/configuracion", "fa-cog"),

                // Accesos ADMIN
                crearAcceso("Matrículas", "/matriculas", "fa-clipboard-list"),
                crearAcceso("Importar Excel", "/matriculas/importar", "fa-file-excel"),
                crearAcceso("Sedes", "/sedes", "fa-building"),
                crearAcceso("Facultades", "/facultades", "fa-university"),
                crearAcceso("Programas", "/programas", "fa-graduation-cap"),
                crearAcceso("Reportes", "/reportes", "fa-chart-bar"),
                crearAcceso("Dashboard Admin", "/dashboard/admin", "fa-tachometer-alt"),

                // Accesos LIDER
                crearAcceso("Dashboard Líder", "/dashboard/lider", "fa-chart-line"),

                // Accesos INTEGRANTE
                crearAcceso("Dashboard Integrante", "/dashboard/integrante", "fa-chart-pie")
        );

        accesos.forEach(acceso -> {
            if (!accesoRepository.existsByUrl(acceso.getUrl())) {
                accesoRepository.save(acceso);
                log.info("Acceso creado: {} - {}", acceso.getNombre(), acceso.getUrl());
            }
        });
    }

    private Acceso crearAcceso(String nombre, String url, String icono) {
        return Acceso.builder()
                .nombre(nombre)
                .url(url)
                .icono(icono)
                .build();
    }

    private void asignarAccesosARoles() {
        // SUPERADMIN: acceso a todo
        asignarTodosLosAccesos(Rol.RolNombre.SUPERADMIN);

        // ADMIN: accesos específicos
        asignarAccesosPorNombres(Rol.RolNombre.ADMIN, Arrays.asList(
                "Matrículas", "Importar Excel", "Sedes", "Facultades",
                "Programas", "Reportes", "Dashboard Admin"
        ));

        // LIDER: dashboard líder
        asignarAccesosPorNombres(Rol.RolNombre.LIDER, Arrays.asList(
                "Dashboard Líder"
        ));

        // INTEGRANTE: dashboard integrante
        asignarAccesosPorNombres(Rol.RolNombre.INTEGRANTE, Arrays.asList(
                "Dashboard Integrante"
        ));
    }

    private void asignarTodosLosAccesos(Rol.RolNombre rolNombre) {
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

        List<Acceso> todosLosAccesos = accesoRepository.findAll();

        todosLosAccesos.forEach(acceso -> {
            if (!accesoRolRepository.existsByRolAndAcceso(rol, acceso)) {
                AccesoRol accesoRol = AccesoRol.builder()
                        .rol(rol)
                        .acceso(acceso)
                        .build();
                accesoRolRepository.save(accesoRol);
                log.debug("Acceso '{}' asignado a rol '{}'", acceso.getNombre(), rolNombre);
            }
        });

        log.info("Todos los accesos asignados a: {}", rolNombre);
    }

    private void asignarAccesosPorNombres(Rol.RolNombre rolNombre, List<String> nombresAccesos) {
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

        nombresAccesos.forEach(nombreAcceso -> {
            Acceso acceso = accesoRepository.findByNombre(nombreAcceso)
                    .orElse(null);

            if (acceso != null && !accesoRolRepository.existsByRolAndAcceso(rol, acceso)) {
                AccesoRol accesoRol = AccesoRol.builder()
                        .rol(rol)
                        .acceso(acceso)
                        .build();
                accesoRolRepository.save(accesoRol);
                log.debug("Acceso '{}' asignado a rol '{}'", nombreAcceso, rolNombre);
            }
        });

        log.info("Accesos asignados a {}: {}", rolNombre, nombresAccesos);
    }
}