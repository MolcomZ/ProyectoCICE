package jpa.comunidades_autonomas;

import jpa.provincias.ProvinciaEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="comunidades")
public class ComunidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length = 45)
    private String nombre;
    @OneToMany(targetEntity = ProvinciaEntity.class, mappedBy = "comunidad",orphanRemoval = true)
    private Collection provincias;

    public ComunidadEntity() {
    }

    public ComunidadEntity(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public Collection getProvincias() {
        return provincias;
    }

    public void setProvincias(Collection provincias) {
        this.provincias = provincias;
    }
}
