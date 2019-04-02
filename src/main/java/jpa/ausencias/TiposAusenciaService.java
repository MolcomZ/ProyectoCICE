package jpa.ausencias;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TiposAusenciaService {
    protected EntityManager em;

    public TiposAusenciaService(EntityManager em) {
        this.em=em;
    }

    public TiposAusenciaEntity createTiposAusencia(Long id, String nombre, String descripcion) {
        em.getTransaction().begin();
        TiposAusenciaEntity entity=new TiposAusenciaEntity(id,nombre,descripcion);
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }
    public TiposAusenciaEntity findTiposAusencia(Long id){
        return em.find(TiposAusenciaEntity.class,id);
    }
    public void removeTiposAusencia(Long id){
        em.getTransaction().begin();
        TiposAusenciaEntity entity=em.find(TiposAusenciaEntity.class,id);
        if(entity!=null){
            em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateTiposAusencia(Long id,String nombre,String descripcion){
        em.getTransaction().begin();
        TiposAusenciaEntity entity=em.find(TiposAusenciaEntity.class,id);
        if(entity!=null){
            entity.setNombre(nombre);
            entity.setDescripcion(descripcion);
            em.persist(entity);
        }
        em.getTransaction().commit();
    }
    public List<TiposAusenciaEntity> findAllTiposAusencias(){
        TypedQuery query=em.createQuery("SELECT e FROM TiposAusenciaEntity e",TiposAusenciaEntity.class);
        return ((TypedQuery) query).getResultList();
    }
}
