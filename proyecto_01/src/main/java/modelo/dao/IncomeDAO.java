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
import modelo.entidades.User;

public class IncomeDAO extends MovementDAO {

	
	EntityManager em = null;
	
	public IncomeDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Movement> findMovementsByCategoryIncome(int idCategory, int idUser,Timestamp startDate, Timestamp endDate) {
		 String sql = "SELECT m.* FROM movimiento m " +
                 "JOIN cuenta c ON m.destino = c.idCuenta " +
                 "WHERE m.categoria = ?1 " +
                 "AND c.propietario = ?2 " +
                 "AND m.fecha BETWEEN ?3 AND ?4";

	    // Crear la consulta nativa
	    Query query = em.createNativeQuery(sql, Movement.class)
	                     .setParameter(1, idCategory)
	                     .setParameter(2, idUser)
	                     .setParameter(3, startDate)
	                     .setParameter(4, endDate);
	    
	    // Ejecutar la consulta y obtener los resultados
	    List<Movement> movements = query.getResultList();
	    return movements;
	}
	
	public List<Movement> getMovementsByAccount(Account account, Timestamp startDate, Timestamp endDate) {
	    try {
	        // JPQL para obtener movimientos por la cuenta y el rango de fechas
	        String jpql = "SELECT i FROM Income i WHERE i.destiny = :cuenta " +
	                      "AND i.date BETWEEN :fechaInicio AND :fechaFin";
	        
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
	
	public void createIncome(Income income) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(income); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear el ingreso", e);
        }
    }
	
	
	public Integer getAccountIdByIncomeId(int idIncome) {
		String query = "SELECT m.destiny.idAccount FROM Income m WHERE m.idMovement = :idIngreso";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idIngreso", idIncome);
         
        return typedQuery.getSingleResult();
    }

	public Integer getCategoryIdByIncomeId(int idMovement) {
		String query = "SELECT m.category.idCategory FROM Income m WHERE m.idMovement = :idMovimiento";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idMovimiento", idMovement);
         
        return typedQuery.getSingleResult();
	}

	public void updateIncome(Income income) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(income); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al actualizar el ingreso", e);
        }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//Lo necesito para el service web por que me da pereza mandar 3 parametros xd
		public List<Income> getAllMovementsIncome() {
	        
			return em.createQuery("SELECT m FROM Income m", Income.class).getResultList();
	    }
	
	
	
	
	
	
	
	
	
	
}
