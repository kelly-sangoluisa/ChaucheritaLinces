
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class test {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin(); // Comienza la transacción
        try {
            // Realiza una consulta SQL nativa para obtener un solo usuario
            Object[] resultado = (Object[]) em.createNativeQuery("SELECT * FROM usuario LIMIT 1").getSingleResult();
            
            // Procesa el resultado
            if (resultado != null) {
                Integer id = (Integer) resultado[0];
                String nombre = (String) resultado[1];
                String clave= (String) resultado[2];
                
                // Imprime los resultados
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Saldo: " + clave);
            } else {
                System.out.println("No se encontraron usuarios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.getTransaction().commit(); // Finaliza la transacción
        }


	}

}
