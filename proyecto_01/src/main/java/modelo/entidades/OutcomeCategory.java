package modelo.entidades;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("C_EGRESO")
public class OutcomeCategory extends Category {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutcomeCategory() {
		super();

	}

	public OutcomeCategory(int idCategory, String name) {
		super(idCategory, name);
		// TODO Auto-generated constructor stub
	}
	
	
	

	
}
