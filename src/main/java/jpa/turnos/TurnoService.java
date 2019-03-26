package jpa.turnos;

import jpa.turnos.TurnoEntity;
import jpa.secciones.SeccionEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TurnoService {
	protected EntityManager em;
	
	public TurnoService(EntityManager em) {
		this.em=em;
	}
	
	public TurnoEntity createTurno(Long id, String nombre, String descripcion) {
		em.getTransaction().begin();
		TurnoEntity entity=new TurnoEntity();
		entity.setNombre(nombre);
		entity.setDescripcion(descripcion);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public TurnoEntity findTurno(Long id){
	    return em.find(TurnoEntity.class,id);
    }
    public void removeTurno(Long id){
		em.getTransaction().begin();
	    TurnoEntity entity=em.find(TurnoEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateTurno(Long id,String nombre,String descripcion){
		em.getTransaction().begin();
	    TurnoEntity entity=em.find(TurnoEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        entity.setDescripcion(descripcion);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<TurnoEntity> findAllTurnos(){
	    TypedQuery query=em.createQuery("SELECT e FROM TurnoEntity e",TurnoEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
}
