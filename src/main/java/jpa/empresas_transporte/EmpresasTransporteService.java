package jpa.empresas_transporte;

import jpa.localidades.LocalidadEntity;
import jpa.provincias.ProvinciaEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmpresasTransporteService {
	protected EntityManager em;

	public EmpresasTransporteService(EntityManager em) {
		this.em=em;
	}
	
	public EmpresasTransporteEntity createEmpresaTransporte(String nombre,String alias,String nif) {
	    EmpresasTransporteEntity entity=new EmpresasTransporteEntity();
        entity.setNombre(nombre);
        entity.setAlias(alias);
        entity.setNif(nif);
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public EmpresasTransporteEntity findEmpresaTransporte(Long id){
	    return em.find(EmpresasTransporteEntity.class,id);
    }
    public void removeEmpresaTransporte(Long id){
		em.getTransaction().begin();
	    LocalidadEntity entity=em.find(LocalidadEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateEmpresaTransporte(Long id,String nombre,String alias,String nif){
		em.getTransaction().begin();
	    EmpresasTransporteEntity entity=em.find(EmpresasTransporteEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        entity.setAlias(alias);
	        entity.setNif(nif);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<EmpresasTransporteEntity> findAllEmpresasTransporte(){
	    TypedQuery query=em.createQuery("SELECT e FROM EmpresasTransporteEntity e",EmpresasTransporteEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
}
