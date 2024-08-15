package modelo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import modelo.entidades.User;

public class UserDAO  {

	EntityManager em = null;
	
	public UserDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	 public User findUsuarioById(int id) {
	        return em.find(User.class, id);
	    }
	
	 
	 public  User authenticate(String username,String password) {
			
		 try {
			 String jpql = "SELECT u FROM User u WHERE u.userName = :username AND u.password = :password";
				Query consulta =  em.createQuery(jpql, User.class);
				consulta.setParameter("username", username);
				consulta.setParameter("password", password);
				User usuarioAutenticado =  (User) consulta.getSingleResult();
				return usuarioAutenticado;
		 }catch(NoResultException e) {
			 return null;
		 	}catch(Exception e) {
		 		e.printStackTrace();
		 		return null;
			 	}
		
			
			
			
		}
	 
	 public  void createUser(User user) {
			em.getTransaction().begin();
			try {
				em.persist(user);
				em.getTransaction().commit();
				
			}catch(Exception e){
				if(em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		} 
}
