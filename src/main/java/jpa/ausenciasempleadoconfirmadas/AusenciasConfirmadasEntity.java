package jpa.ausenciasempleadoconfirmadas;

import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausenciasempleado.AusenciasEmpleadoEntity;
import jpa.empleados.EmpleadoEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="ausencias_confirmadas")
public class AusenciasConfirmadasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idausencia")
    private AusenciasEmpleadoEntity ausencia;
    @Column(name="inicio")
    private Date inicio;
    @Column(name="fin")
    private Date fin;

    public AusenciasConfirmadasEntity() {
    }

    public AusenciasConfirmadasEntity(Long id, AusenciasEmpleadoEntity ausencia, Date inicio, Date fin) {
        this.id = id;
        this.ausencia = ausencia;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AusenciasEmpleadoEntity getAusencia() {
        return ausencia;
    }

    public void setAusencia(AusenciasEmpleadoEntity ausencia) {
        this.ausencia = ausencia;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AusenciasConfirmadasEntity that = (AusenciasConfirmadasEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ausencia, that.ausencia) &&
                Objects.equals(inicio, that.inicio) &&
                Objects.equals(fin, that.fin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ausencia, inicio, fin);
    }

    @Override
    public String toString() {
        return "AusenciasConfirmadasEntity{" +
                "id=" + id +
                ", ausencia=" + ausencia +
                ", inicio=" + inicio +
                ", fin=" + fin +
                '}';
    }
}
