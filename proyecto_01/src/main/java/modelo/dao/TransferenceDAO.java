package modelo.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Category;
import modelo.entidades.Account;
import modelo.entidades.Outcome;
import modelo.entidades.Movement;
import modelo.entidades.Transference;

public class TransferenceDAO  extends MovementDAO {
	EntityManager em = null;
	
	public TransferenceDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Movement> findMovementsByCategoryTransference(int idCategory, int idUser ,Timestamp startDate, Timestamp endDate) {
		String sql = "SELECT m.* FROM movimiento m JOIN cuenta c ON m.origen = c.idCuenta WHERE m.categoria = ?  AND c.propietario = ?"
					+ " AND m.fecha BETWEEN ?3 AND ?4 ;";
	    Query query = em.createNativeQuery(sql, Movement.class)
	             .setParameter(1, idCategory)
	             .setParameter(2, idUser)
	             .setParameter(3, startDate)
			     .setParameter(4, endDate);
	    List<Movement> movement = query.getResultList();
	    return movement;
	}
	
	
	public List<Movement> getMovementsByAccount(Account account, Timestamp startDate, Timestamp endDate) {
	    try {
	        // JPQL para obtener movimientos por la cuenta y el rango de fechas
	        String jpql = "SELECT t FROM Transference t WHERE t.origin = :cuenta " +
	                      "AND t.date BETWEEN :fechaInicio AND :fechaFin";
	        
	        // Crear la consulta
	        TypedQuery<Movement> query = em.createQuery(jpql, Movement.class);
	        
	        // Establecer los parámetros de consulta
	        query.setParameter("cuenta", account);
	        query.setParameter("fechaInicio", startDate);
	        query.setParameter("fechaFin", endDate);
	        
	        // Ejecutar la consulta y devolver los resultados
	        return query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyList(); // Retornar una lista vacía en caso de error
	    }
	}

	public void createTransference(Transference transference) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(transference); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear la Transferencia", e);
        }
    }

	public Integer getDestinyIdByTransferenceId(int idMovement) {
		String query = "SELECT m.destiny.idAccount FROM Transference m WHERE m.idMovement = :idTransferencia";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idTransferencia", idMovement);
         
        return typedQuery.getSingleResult();
	}

	public Integer getCategoryIdTransferenceId(int idMovement) {
		String query = "SELECT m.category.idCategory FROM Transference m WHERE m.idMovement = :idMovimiento";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idMovimiento", idMovement);
         
        return typedQuery.getSingleResult();
	}

	public Integer getOriginIdByTransferenceId(int idMovement) {
		String query = "SELECT m.origin.idAccount FROM Transference m WHERE m.idMovement = :idTransferencia";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idTransferencia", idMovement);
         
        return typedQuery.getSingleResult();
	}

	public void updateTransferencia(Transference transference) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(transference); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al actualizar la transferencia", e);
        }
		
	}
	
	
	
	
	
	
	
	
	
public List<Transference> getAllMovementsTransference() {
        
		return em.createQuery("SELECT m FROM Transference m", Transference.class).getResultList();
    }
	
	
	
	
	
	
	
	
}
