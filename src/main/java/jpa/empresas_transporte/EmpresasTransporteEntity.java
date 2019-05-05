package jpa.empresas_transporte;

import jpa.provincias.ProvinciaEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="empresas_transporte")
public class EmpresasTransporteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45)
    private String nombre;
    @Column(length = 15)
    private String alias;
    @Column(length = 9)
    private String nif;

    public EmpresasTransporteEntity() {
    }

    public EmpresasTransporteEntity(Long id, String nombre, String alias, String nif) {
        this.id = id;
        this.nombre = nombre;
        this.alias = alias;
        this.nif = nif;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresasTransporteEntity that = (EmpresasTransporteEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(nif, that.nif);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nombre, alias, nif);
    }

    @Override
    public String toString() {
        return "EmpresasTransporteEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", alias='" + alias + '\'' +
                ", nif='" + nif + '\'' +
                '}';
    }
}
