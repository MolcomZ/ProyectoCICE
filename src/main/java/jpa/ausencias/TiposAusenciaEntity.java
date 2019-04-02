package jpa.ausencias;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="tiposausencia")
public class TiposAusenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre")
    private String nombre;
    @Column(name="descripcion")
    private String descripcion;

    public TiposAusenciaEntity(){

    }

    public TiposAusenciaEntity(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposAusenciaEntity that = (TiposAusenciaEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nombre, descripcion);
    }
}
