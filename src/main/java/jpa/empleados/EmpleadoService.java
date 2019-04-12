package jpa.empleados;

import jpa.puestos.PuestoEntity;
import jpa.turnos.TurnoEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmpleadoService {
	protected EntityManager em;
	
	public EmpleadoService(EntityManager em) {
		this.em=em;
	}
	
	public EmpleadoEntity createEmpleado(Long id, String nombre, String apellido, TurnoEntity turno, PuestoEntity puesto) {
		em.getTransaction().begin();
		EmpleadoEntity entity=new EmpleadoEntity(id,nombre,apellido,turno,puesto);
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
	public EmpleadoEntity findEmpleado(Long id){
	    return em.find(EmpleadoEntity.class,id);
    }
    public void removeEmpleado(Long id){
		em.getTransaction().begin();
	    EmpleadoEntity entity=em.find(EmpleadoEntity.class,id);
	    if(entity!=null){
	        em.remove(entity);
        }
        em.getTransaction().commit();
    }
    public void updateEmpleado(Long id,String nombre,String apellido,TurnoEntity turno,PuestoEntity puesto){
		em.getTransaction().begin();
	    EmpleadoEntity entity=em.find(EmpleadoEntity.class,id);
	    if(entity!=null){
	        entity.setNombre(nombre);
	        entity.setApellido(apellido);
	        entity.setTurno(turno);
	        entity.setPuesto(puesto);
	        em.persist(entity);
        }
        em.getTransaction().commit();
    }
	public void updateEmpleado(Long id,Long newid,String nombre,String apellido,TurnoEntity turno,PuestoEntity puesto){
		em.getTransaction().begin();
		EmpleadoEntity entity=em.find(EmpleadoEntity.class,id);
		if(entity!=null){
		    entity.setId(newid);
			entity.setNombre(nombre);
			entity.setApellido(apellido);
			entity.setTurno(turno);
			entity.setPuesto(puesto);
			em.persist(entity);
		}
		em.getTransaction().commit();
	}
    public List<EmpleadoEntity> findAllEmpleados(){
	    TypedQuery query=em.createQuery("SELECT e FROM EmpleadoEntity e",EmpleadoEntity.class);
	    return ((TypedQuery) query).getResultList();
    }
}
