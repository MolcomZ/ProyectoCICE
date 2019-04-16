package jpa.ausenciasempleadoconfirmadas;

import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausenciasempleado.AusenciasEmpleadoEntity;
import jpa.empleados.EmpleadoEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class AusenciasConfirmadasService {
    protected EntityManager em;

    public AusenciasConfirmadasService(EntityManager em) {
        this.em = em;
    }
    public AusenciasConfirmadasEntity createAusenciasConfirmadas(AusenciasEmpleadoEntity ausencia, Date inicio,Date fin) {
        em.getTransaction().begin();
        AusenciasConfirmadasEntity entity=new AusenciasConfirmadasEntity(null,ausencia,inicio,fin);
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }
    public AusenciasConfirmadasEntity findAusenciasConfirmadas(Long id){
        return em.find(AusenciasConfirmadasEntity.class,id);
    }
    public void removeAusenciasConfirmadas(Long id){
        em.getTransaction().begin();
        AusenciasConfirmadasEntity entity=em.find(AusenciasConfirmadasEntity.class,id);
        if(entity!=null){
            em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateAusenciasConfirmadas(Long id,AusenciasEmpleadoEntity ausencia,Date inicio,Date fin){
        em.getTransaction().begin();
        AusenciasConfirmadasEntity entity=em.find(AusenciasConfirmadasEntity.class,id);
        if(entity!=null){
            entity.setAusencia(ausencia);
            entity.setInicio(inicio);
            entity.setFin(fin);
            em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<AusenciasConfirmadasEntity> findAusenciasConfirmadasByIdAusenciaEmpleado(Long idAusenciaEmpleado){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasConfirmadasEntity e WHERE e.ausencia.id="+idAusenciaEmpleado,AusenciasConfirmadasEntity.class);
        return ((TypedQuery) query).getResultList();
    }
    public List<AusenciasConfirmadasEntity> findAllAusenciasConfirmadass(){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasConfirmadasEntity e",AusenciasConfirmadasEntity.class);
        return ((TypedQuery) query).getResultList();
    }
    public List<AusenciasConfirmadasEntity> findAusenciasConfirmadasesByIdEmpleado(Long idempleado){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasConfirmadasEntity e WHERE e.ausencia.empleado.id = "+idempleado,AusenciasConfirmadasEntity.class);
        return ((TypedQuery)query).getResultList();
    }
    public List<AusenciasConfirmadasEntity> findAusenciasConfirmadasesByIdEmpleadoAndByYear(Long idempleado,Integer year){
        TypedQuery query=em.createQuery("SELECT e FROM AusenciasConfirmadasEntity e WHERE e.ausencia.empleado.id="+idempleado+" AND e.ausencia.year="+year,AusenciasConfirmadasEntity.class);
        return ((TypedQuery)query).getResultList();
    }
}
