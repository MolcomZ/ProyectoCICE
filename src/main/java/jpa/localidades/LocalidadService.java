package jpa.localidades;

import jpa.provincias.ProvinciaEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class LocalidadService {
	protected EntityManager em;
	
	public LocalidadService(EntityManager em) {
		this.em=em;
	}
	
	public LocalidadEntity createLocalidad(Long id, String nombre, ProvinciaEntity provincia) {
		em.getTransaction().begin();
		LocalidadEntity entity=new LocalidadEntity();
		//entity.setId(id);
		entity.setNombre(nombre);
		entity.setProvincia(provincia);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public LocalidadEntity findLocalidad(Long id){
	    return em.find(LocalidadEntity.class,id);
    }
    public void removeLocalidad(Long id){
		em.getTransaction().begin();
	    LocalidadEntity entity=em.find(LocalidadEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateLocalidad(Long id,String nombre){
		em.getTransaction().begin();
	    LocalidadEntity entity=em.find(LocalidadEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<LocalidadEntity> findAllLocalidades(){
	    TypedQuery query=em.createQuery("SELECT e FROM LocalidadEntity e",LocalidadEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
    public List<LocalidadEntity> findLocalidadesByIdProvincia(Long idprovincia){
	    TypedQuery query=em.createQuery("SELECT e FROM LocalidadEntity e WHERE e.provincia.id="+idprovincia,LocalidadEntity.class);
	    return ((TypedQuery)query).getResultList();
    }
}
