package jpa.puestos;

import jpa.secciones.SeccionEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="puestos")
public class PuestoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="idSeccion")
    private SeccionEntity seccion;

    public PuestoEntity() {
    }

    public PuestoEntity(Long id, String nombre, SeccionEntity seccion) {
        this.id = id;
        this.nombre = nombre;
        this.seccion = seccion;
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

    public SeccionEntity getSeccion() {
        return seccion;
    }

    public void setSeccion(SeccionEntity seccion) {
        this.seccion = seccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuestoEntity entity = (PuestoEntity) o;
        return Objects.equals(id, entity.id);// &&
                //Objects.equals(nombre, entity.nombre) &&
                //Objects.equals(seccion, entity.seccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
