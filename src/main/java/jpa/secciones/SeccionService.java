package jpa.secciones;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class SeccionService {
	protected EntityManager em;
	
	public SeccionService(EntityManager em) {
		this.em=em;
	}
	
	public SeccionEntity createSeccion(Long id, String nombre) {
		em.getTransaction().begin();
		SeccionEntity entity=new SeccionEntity();
		//entity.setId(id);
		entity.setNombre(nombre);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public SeccionEntity findSeccion(Long id){
	    return em.find(SeccionEntity.class,id);
    }
    public void removeSeccion(Long id){
		em.getTransaction().begin();
	    SeccionEntity entity=em.find(SeccionEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateSeccion(Long id,String nombre){
		em.getTransaction().begin();
	    SeccionEntity entity=em.find(SeccionEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<SeccionEntity> findAllSecciones(){
	    TypedQuery query=em.createQuery("SELECT e FROM SeccionEntity e",SeccionEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
}
