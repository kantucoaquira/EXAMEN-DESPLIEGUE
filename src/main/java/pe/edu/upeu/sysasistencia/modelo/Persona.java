package pe.edu.upeu.sysasistencia.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @Column(name = "codigo_estudiante", length = 20, unique = true)
    private String codigoEstudiante;

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @Column(name = "documento", length = 20, unique = true)
    private String documento;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "correo_institucional", length = 100)
    private String correoInstitucional;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "pais", length = 50)
    private String pais;

    @Column(name = "foto", length = 500)
    private String foto;

    @Column(name = "religion", length = 50)
    private String religion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", length = 20, nullable = false)
    private TipoPersona tipoPersona;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
}