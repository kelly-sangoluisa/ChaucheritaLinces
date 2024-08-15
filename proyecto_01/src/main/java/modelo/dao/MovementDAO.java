package modelo.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import java.sql.Timestamp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.dto.MovementDTO;

import modelo.entidades.Category;
import modelo.entidades.Account;
import modelo.entidades.Outcome;
import modelo.entidades.Income;
import modelo.entidades.Movement;
import modelo.entidades.Transference;

public class MovementDAO {

	EntityManager em = null;
	
	public MovementDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void createMovement(Movement movement) {
        em.getTransaction().begin();
        em.persist(movement);
        em.getTransaction().commit();
    }
	
	public Movement findMovementById(int idMovement) {
        return em.find(Movement.class, idMovement);
    }
	
	/*public List<Movement> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Movimiento m", Movement.class).getResultList();
    }*/
	public List<Movement> getAllMovementsByUserId(int idUser, Timestamp startDate, Timestamp endDate) {
		String sql = "SELECT m.* FROM Movimiento m " +
                "LEFT JOIN Cuenta c_origen ON m.origen = c_origen.idCuenta " +
                "LEFT JOIN Cuenta c_destino ON m.destino = c_destino.idCuenta " +
                "WHERE (c_origen.propietario = ? OR c_destino.propietario = ?) " +
                "AND m.fecha BETWEEN ? AND ?";
   
	   Query query = em.createNativeQuery(sql, Movement.class);
	   query.setParameter(1, idUser);
	   query.setParameter(2, idUser);
	   query.setParameter(3, startDate);
	   query.setParameter(4, endDate);
	   return query.getResultList();
	}
	

	
	
	public List<Movement> getMovementsByAccount(int idAccount) {
        try {
            // JPQL para obtener movimientos por el ID de la cuenta
            String sql = "SELECT * FROM movimiento m WHERE (m.origen = ?1 or m.destino = ?1)";
            // Crear la consulta
           Query query = em.createNativeQuery(sql, Movement.class);
            // Establecer el parámetro de consulta
            query.setParameter(1,idAccount);
            
            // Ejecutar la consulta y devolver los resultados
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Registrar o manejar otras excepciones
            return Collections.emptyList(); // Retornar una lista vacía en caso de error
        }
    }
	
	
	 private  MovementDTO toMovementDTO(Movement movement) {
	        int idMovimiento = movement.getIdMovement();
	        String concepto = movement.getConcept();
	        Date fecha = movement.getDate();
	        Double monto = movement.getAmount();
	        
	        String categoria = "";
	        String origen = "";
	        String destino = "";

	        if (movement instanceof Outcome) {
	            Outcome outcome = (Outcome) movement;
	            categoria = outcome.getCategoria().getNameCategory();
	            origen = outcome.getOrigin().getNameAccount(); // Obtener el origen
	            destino = "EGRESO"; // Valor predeterminado para Egreso
	        } else if (movement instanceof Income) {
	            Income income = (Income) movement;
	            categoria  =income.getCategory().getNameCategory();
	            destino = income.getDestiny().getNameAccount(); // Obtener el destino
	            origen = "INGRESO"; // Valor predeterminado para Ingreso
	        }else if (movement instanceof Transference) {
	            Transference transference = (Transference) movement;
	            categoria = transference.getCategory().getNameCategory();
	            origen = transference.getOrigin().getNameAccount(); // Obtener la cuenta de origen
	            destino = transference.getDestiny().getNameAccount(); // Obtener la cuenta de destino
	             // Valor predeterminado para Transferencia
	        }

	        return new MovementDTO(idMovimiento, concepto, fecha, monto, categoria, origen, destino);
	    }
	    
	    
	    public List<MovementDTO> getAllMovementsDTO(List<Movement> movements) {
	        List<MovementDTO> movimientosDTO = new ArrayList<>();

	        for (Movement movimientoNew : movements) {
	            MovementDTO dto = toMovementDTO(movimientoNew);
	            movimientosDTO.add(dto);
	        }

	        return movimientosDTO;
	    }
	
	public void deleteMovement(int idMovement) {
		EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();


            // Buscar la categoría por su ID
            Movement movement = em.find(Movement.class, idMovement);
            
            if (movement != null) {
                // Eliminar la categoría
                em.remove(movement);
            } else {
                throw new IllegalArgumentException("Categoría con ID " + idMovement + " no encontrada.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar la categoría", e);
        }
	}


	/*public List<Movimiento> findMovimientosByCategoria(Categoria categoria) {
	    try {
	        // Crear la consulta JPQL para obtener movimientos asociados a la categoría
	        String jpql = "SELECT m FROM Movimiento m WHERE m.categoria = :categoria";
	        TypedQuery<Movimiento> query = em.createQuery(jpql, Movimiento.class);
	        query.setParameter("categoria", categoria);
	        
	        // Ejecutar la consulta y devolver los resultados
	        return query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyList(); // Retornar una lista vacía en caso de error
	    }
	}*/

	
	
}
