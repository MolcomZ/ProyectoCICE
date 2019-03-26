package jpa.empleados;

import jpa.puestos.PuestoEntity;
import jpa.turnos.TurnoEntity;

import javax.persistence.*;

@Entity
@Table(name="empleados")
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre")
    private String nombre;
    @Column(name="apellido")
    private String apellido;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idturno")
    private TurnoEntity turno;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idpuesto")
    private PuestoEntity puesto;

    public EmpleadoEntity() {
    }

    public EmpleadoEntity(Long id, String nombre, String apellido, TurnoEntity turno, PuestoEntity puesto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
        this.puesto = puesto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TurnoEntity getTurno() {
        return turno;
    }

    public void setTurno(TurnoEntity turno) {
        this.turno = turno;
    }

    public PuestoEntity getPuesto() {
        return puesto;
    }

    public void setPuesto(PuestoEntity puesto) {
        this.puesto = puesto;
    }
}
