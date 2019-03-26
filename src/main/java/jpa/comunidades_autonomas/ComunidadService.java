package jpa.comunidades_autonomas;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ComunidadService {
	protected EntityManager em;
	
	public ComunidadService(EntityManager em) {
		this.em=em;
	}
	
	public ComunidadEntity createComunidad(Long id, String nombre) {
		em.getTransaction().begin();
		ComunidadEntity entity=new ComunidadEntity();
		//entity.setId(id);
		entity.setNombre(nombre);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public ComunidadEntity findComunidad(Long id){
	    return em.find(ComunidadEntity.class,id);
    }
    public void removeComunidad(Long id){
		em.getTransaction().begin();
	    ComunidadEntity entity=em.find(ComunidadEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateComunidad(Long id,String nombre){
		em.getTransaction().begin();
	    ComunidadEntity entity=em.find(ComunidadEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<ComunidadEntity> findAllComunidades(){
	    TypedQuery query=em.createQuery("SELECT e FROM ComunidadEntity e",ComunidadEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
}
