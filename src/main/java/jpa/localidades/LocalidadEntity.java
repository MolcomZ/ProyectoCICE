package jpa.localidades;

import jpa.provincias.ProvinciaEntity;

import javax.persistence.*;

@Entity
@Table(name="localidades")
public class LocalidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="idprovincia")
    private ProvinciaEntity provincia;

    public LocalidadEntity() {
    }

    public LocalidadEntity(Long id, String nombre) {
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

    public ProvinciaEntity getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaEntity provincia) {
        this.provincia = provincia;
    }
}
