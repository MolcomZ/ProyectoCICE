package jpa.ausenciasempleado;

import jpa.ausencias.TiposAusenciaEntity;
import jpa.empleados.EmpleadoEntity;
import org.hibernate.SQLQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AusenciasEmpleadoService {
    protected EntityManager em;

    public AusenciasEmpleadoService(EntityManager em) {
        this.em = em;
    }
    public AusenciasEmpleadoEntity createAusenciasEmpleado(EmpleadoEntity empleado, TiposAusenciaEntity tipo, Integer year, Integer cantidad) {
        em.getTransaction().begin();
        AusenciasEmpleadoEntity entity=new AusenciasEmpleadoEntity(null,empleado,tipo,year,cantidad);
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }
    public AusenciasEmpleadoEntity findAusenciasEmpleado(Long id){
        return em.find(AusenciasEmpleadoEntity.class,id);
    }
    public void removeAusenciasEmpleado(Long id){
        em.getTransaction().begin();
        AusenciasEmpleadoEntity entity=em.find(AusenciasEmpleadoEntity.class,id);
        if(entity!=null){
            em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateAusenciasEmpleado(Long id,TiposAusenciaEntity tipo,Integer year,Integer cantidad){
        em.getTransaction().begin();
        AusenciasEmpleadoEntity entity=em.find(AusenciasEmpleadoEntity.class,id);
        if(entity!=null){
            entity.setTipo(tipo);
            entity.setYear(year);
            entity.setCantidad(cantidad);
            em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<AusenciasEmpleadoEntity> findAllAusenciasEmpleados(){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasEmpleadoEntity e",AusenciasEmpleadoEntity.class);
        return ((TypedQuery) query).getResultList();
    }
    public List<AusenciasEmpleadoEntity> findAusenciasEmpleadoesByIdEmpleado(Long idempleado){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasEmpleadoEntity e WHERE e.empleado.id = "+idempleado,AusenciasEmpleadoEntity.class);
        return ((TypedQuery)query).getResultList();
    }
    public List<AusenciasEmpleadoEntity> findAusenciasEmpleadoesByIdEmpleadoAndByYear(Long idempleado,Integer year){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasEmpleadoEntity e WHERE e.empleado.id="+idempleado+" AND e.year="+year,AusenciasEmpleadoEntity.class);
        return ((TypedQuery)query).getResultList();
    }
}
