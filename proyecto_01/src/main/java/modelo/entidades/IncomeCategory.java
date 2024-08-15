package modelo.entidades;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("C_INGRESO")

public class IncomeCategory extends Category {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncomeCategory() {
		super();

	}

	public IncomeCategory(int idCategoria, String name) {
		super(idCategoria, name);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
