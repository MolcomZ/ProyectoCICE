package jpa.provincias;

import jpa.comunidades_autonomas.ComunidadEntity;

import javax.persistence.*;

@Entity
@Table(name="provincias")
public class ProvinciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="idcomunidad")
    private ComunidadEntity comunidad;

    public ProvinciaEntity() {
    }

    public ProvinciaEntity(Long id, String nombre) {
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

    public ComunidadEntity getComunidad() {
        return comunidad;
    }

    public void setComunidad(ComunidadEntity comunidad) {
        this.comunidad = comunidad;
    }
}
