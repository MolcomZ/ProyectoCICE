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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AusenciasEmpleadoEntity that = (AusenciasEmpleadoEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(year, that.year) &&
                Objects.equals(cantidad, that.cantidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, year, cantidad);
    }

    @Override
    public String toString() {
        return "AusenciasEmpleadoEntity{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", year=" + year +
                ", cantidad=" + cantidad +
                '}';
    }
}
