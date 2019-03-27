package jpa.puestos;

import jpa.secciones.SeccionEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PuestoService {
	protected EntityManager em;
	
	public PuestoService(EntityManager em) {
		this.em=em;
	}
	
	public PuestoEntity createPuesto(Long id, String nombre, SeccionEntity seccion) {
		em.getTransaction().begin();
		PuestoEntity entity=new PuestoEntity();
		entity.setNombre(nombre);
		entity.setSeccion(seccion);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public PuestoEntity findPuesto(Long id){
	    return em.find(PuestoEntity.class,id);
    }
    public void removePuesto(Long id){
		em.getTransaction().begin();
	    PuestoEntity entity=em.find(PuestoEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updatePuesto(Long id,String nombre,SeccionEntity seccion){
		em.getTransaction().begin();
	    PuestoEntity entity=em.find(PuestoEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        entity.setSeccion(seccion);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<PuestoEntity> findAllPuestos(){
	    TypedQuery query=em.createQuery("SELECT e FROM PuestoEntity e",PuestoEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
	public List<PuestoEntity> findPuestosByIdSeccion(Long idseccion){
		TypedQuery query=em.createQuery("SELECT e FROM PuestoEntity e WHERE e.seccion.id="+idseccion,PuestoEntity.class);
		return ((TypedQuery)query).getResultList();
	}
}
