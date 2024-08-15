package modelo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import modelo.entidades.Account;

public class AccountDAO  {

	EntityManager em = null;
	
	public AccountDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void createAccount(Account account) {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }
	
	public List<Account> getAllAccountsByUserId(int idUser) {
	    String sql = "SELECT * FROM Cuenta WHERE propietario = ?";
	    Query query = em.createNativeQuery(sql, Account.class);
	    query.setParameter(1, idUser);
	    return query.getResultList();
	}

	
	public  List<Account> getAllAccounts() {
		return em.createQuery("SELECT c FROM Account c", Account.class).getResultList();
		
	}
	public Account getAccountById(int idAccount) {
        return em.find(Account.class, idAccount);
    }
	
	
	public boolean updateTotal(int idAccount, double amount) throws Exception {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            
            Account account = em.find(Account.class, idAccount);
            if (account != null) {
                // Actualiza el saldo
            	double nuevoSaldo = account.getTotal() + amount;
            	if (nuevoSaldo < 0) {
                    // Si el saldo es negativo, cancela la transacción y lanza una excepción
            		
                    transaction.rollback();
                    throw new Exception("El valor ingresado supera el saldo total de la cuenta.");
                }
            	
                account.setTotal(nuevoSaldo);
                em.merge(account);
                // Sincroniza el estado de la entidad con la base de datos
            }
            
            transaction.commit();
            return true;
        } catch (Exception e) {
        	 if (transaction.isActive()) {
                 transaction.rollback();
                 return false;
             }
             throw e;
        }
    }
	
	//IGNORAR
	public void delete(int idCuenta) {
		
		EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Buscar la cuenta por su ID
            Account account = em.find(Account.class, idCuenta);
            
            if (account != null) {
                // Eliminar la cuenta
                em.remove(account);
            } else {
                throw new IllegalArgumentException("Cuenta con ID " + idCuenta + " no encontrada.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar la cuenta", e);
        } finally {
            em.close();
        }
		
	}
	
	
	
	
}
