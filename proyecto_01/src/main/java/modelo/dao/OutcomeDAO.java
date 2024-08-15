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
import modelo.entidades.Income;
import modelo.entidades.Movement;

public class OutcomeDAO extends MovementDAO {

	EntityManager em = null;
	
	public OutcomeDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	public List<Movement> findMovementsByOutcomeCategory(int category, int idUser,Timestamp startDate, Timestamp endDate) {
		String sql = "SELECT m.* FROM movimiento m JOIN cuenta c ON m.origen = c.idCuenta WHERE m.categoria = ?1  AND c.propietario = ?2"
				 + " AND m.fecha BETWEEN ?3 AND ?4 ;" ;
	    Query query = em.createNativeQuery(sql, Movement.class)
	             .setParameter(1, category)
	             .setParameter(2, idUser)
				 .setParameter(3, startDate)
			     .setParameter(4, endDate);

	    List<Movement> movement = query.getResultList();
	    return movement;
	}
	
	
	public List<Movement> getMovementsByAccount(Account account, Timestamp startDate, Timestamp endDate) {
	    try {
	        // JPQL para obtener movimientos por la cuenta y el rango de fechas
	        String jpql = "SELECT e FROM Outcome e WHERE e.origin = :cuenta " +
	                      "AND e.date BETWEEN :fechaInicio AND :fechaFin";
	        
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
	
	public void createOutcome(Outcome outcome , AccountDAO accountDAO, Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            accountDAO.updateTotal(account.getIdAccount(), outcome.getAmount());
            em.persist(outcome); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear el egreso", e);
        }
    }
	public Integer getAccountIdByOutcomeId(int idMovement) {
		String query = "SELECT m.origin.idAccount FROM Outcome m WHERE m.idMovement = :idEgreso";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idEgreso", idMovement);
         
        return typedQuery.getSingleResult();
	}
	public Integer getCategoryIdByOutcomeId(int idMovement) {
		String query = "SELECT m.category.idCategory FROM Outcome m WHERE m.idMovement = :idMovimiento";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idMovimiento", idMovement);
         
        return typedQuery.getSingleResult();
	}
	public void updateOutcome(Outcome outcome) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(outcome); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al actualizar el egreso", e);
        }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Lo necesito para el service web por que me da pereza mandar 3 parametros xd
		public List<Outcome> getAllMovementsEgreso() {
	        
			return em.createQuery("SELECT m FROM Outcome m", Outcome.class).getResultList();
	    }
	
	
	
	
	
}
