package modelo.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Category;
import modelo.entidades.Account;

public class CategoryDAO {
	EntityManager em = null;
	
	public CategoryDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void saveCategory(Category category) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Category findCategoryById(int id) {
        return em.find(Category.class, id);
    }
    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }
    public List<Category> findAllByUserId(int idUser) {
        try {
            // SQL query para obtener todas las categorías asociadas a un usuario específico
            String sql = "SELECT DISTINCT c.* " +
                         "FROM Categoria c " +
                         "JOIN Movimiento m ON c.IDCATEGORIA = m.categoria " +
                         "JOIN Cuenta cu ON m.origen = cu.idCuenta OR m.destino = cu.idCuenta " +
                         "WHERE cu.propietario = ?";
            
            // Crear la consulta nativa
            Query query = em.createNativeQuery(sql, Category.class);
            query.setParameter(1, idUser); // Establecer el parámetro del id del usuario
            
            // Ejecutar la consulta y obtener los resultados
            List<Category> categories = query.getResultList();
            
            return categories;
            
        } catch (Exception e) {
            e.printStackTrace(); // Registrar o manejar otras excepciones
            return Collections.emptyList(); // Retornar una lista vacía en caso de error
        }
    }

    
    
    /*public List<Category> getCategoriesByType(String tipo) {
        return em.createQuery("SELECT c FROM Categoria c WHERE c.categoriaType = :tipo", Category.class)
                 .setParameter("tipo", tipo)
                 .getResultList();
    }*/
    /*public List<Category> getCategories() {
        TypedQuery<Category> query = em.createQuery("SELECT c FROM Categoria c", Category.class);
        return query.getResultList();
    }*/
    public Category getCategoryById(int idCategory) {
        return em.find(Category.class, idCategory);
    }
    
    public void deleteCategory(int idCategory) {
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            System.out.println("Intentando eliminar la categoría con ID: " + idCategory);

            // Buscar la categoría por su ID
            Category category = em.find(Category.class, idCategory);
            
            if (category != null) {
                // Eliminar la categoría
                em.remove(category);
            } else {
                throw new IllegalArgumentException("Categoría con ID " + idCategory + " no encontrada.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar la categoría", e);
        }
    }
	
    
    
}
