package pe.edu.upeu.sysasistencia.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private Long idMatricula;

    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    @ManyToOne
    @JoinColumn(name = "facultad_id", nullable = false)
    private Facultad facultad;

    @ManyToOne
    @JoinColumn(name = "programa_id", nullable = false)
    private ProgramaEstudio programaEstudio;

    @Column(name = "modo_contrato", length = 50)
    private String modoContrato;

    @Column(name = "modalidad_estudio", length = 50)
    private String modalidadEstudio;

    @Column(name = "ciclo", length = 10)
    private String ciclo;

    @Column(name = "grupo", length = 10)
    private String grupo;

    @Column(name = "fecha_matricula")
    private LocalDateTime fechaMatricula;

    @Column(name = "estado", length = 20)
    private String estado;
}