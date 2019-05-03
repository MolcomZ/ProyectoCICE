package jpa.ausenciasempleado;

import jpa.ausencias.TiposAusenciaEntity;
import jpa.empleados.EmpleadoEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="ausencias_empleado")
public class AusenciasEmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idempleado")
    private EmpleadoEntity empleado;
    @ManyToOne
    @JoinColumn(name="idtipo")
    private TiposAusenciaEntity tipo;
    @Column(name="year")
    private Integer year;
    @Column(name="cantidad")
    private Integer cantidad;

    public AusenciasEmpleadoEntity() {
    }

    public AusenciasEmpleadoEntity(Long id,EmpleadoEntity empleado, TiposAusenciaEntity tipo, Integer year, Integer cantidad) {
        this.id = id;
        this.empleado=empleado;
        this.tipo = tipo;
        this.year = year;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TiposAusenciaEntity getTipo() {
        return tipo;
    }

    public void setTipo(TiposAusenciaEntity tipo) {
        this.tipo = tipo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public EmpleadoEntity getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoEntity empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AusenciasEmpleadoEntity entity = (AusenciasEmpleadoEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(empleado, entity.empleado) &&
                Objects.equals(tipo, entity.tipo) &&
                Objects.equals(year, entity.year) &&
                Objects.equals(cantidad, entity.cantidad);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, empleado, tipo, year, cantidad);
    }

    @Override
    public String toString() {
        return "AusenciasEmpleadoEntity{" +
                "id=" + id +
                ", empleado=" + empleado +
                ", tipo=" + tipo +
                ", year=" + year +
                ", cantidad=" + cantidad +
                '}';
    }
}
