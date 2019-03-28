package jpa.provincias;

import jpa.comunidades_autonomas.ComunidadEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProvinciaService {
	protected EntityManager em;
	
	public ProvinciaService(EntityManager em) {
		this.em=em;
	}
	
	public ProvinciaEntity createProvincia(Long id, String nombre, ComunidadEntity comunidad) {
		ProvinciaEntity entity=new ProvinciaEntity();
		entity.setNombre(nombre);
		entity.setComunidad(comunidad);
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public ProvinciaEntity findProvincia(Long id){
	    return em.find(ProvinciaEntity.class,id);
    }
    public void removeProvincia(Long id){
		em.getTransaction().begin();
	    ProvinciaEntity entity=em.find(ProvinciaEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateProvincia(Long id,String nombre){
	    ProvinciaEntity entity=em.find(ProvinciaEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
            em.getTransaction().begin();
	        em.persist(entity);
			em.getTransaction().commit();
        }
    }
    public List<ProvinciaEntity> findAllProvincias(){
	    TypedQuery query=em.createQuery("SELECT e FROM ProvinciaEntity e",ProvinciaEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
    public List<ProvinciaEntity> findProvinciasByIdComunidad(Long idcomunidad){
	    TypedQuery query=em.createQuery("SELECT e FROM ProvinciaEntity e WHERE e.comunidad.id="+idcomunidad,ProvinciaEntity.class);
	    return ((TypedQuery)query).getResultList();
    }
}
