package modelo.entidades;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("C_TRANSFERENCIA")
public class TransferenceCategory extends Category {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransferenceCategory() {
		super();

	}

	public TransferenceCategory(int idCategory, String name) {
		super(idCategory, name);
		// TODO Auto-generated constructor stub
	}
}
