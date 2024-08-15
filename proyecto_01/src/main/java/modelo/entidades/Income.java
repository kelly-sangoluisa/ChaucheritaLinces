package modelo.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("INGRESO")
public class Income extends Movement {  
	


	@ManyToOne
	@JoinColumn(name= "destino")
	private Account destiny;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name= "categoria")
	private IncomeCategory category;
	
	
	

	public Income() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Income(int idMovimiento, String concepto, Date fecha, double valor, IncomeCategory incomeCategory, Account cuentaDestino) {
		super(idMovimiento, concepto, fecha, valor);
		this.category = incomeCategory;
		this.destiny = cuentaDestino;
		// TODO Auto-generated constructor stub
	}
	

	public Account getDestiny() {
		return destiny;
	}

	public void setDestiny(Account destino) {
		this.destiny = destino;
	}

	public IncomeCategory getCategory() {
		return category;
	}

	public void setCategory(IncomeCategory incomeCategory) {
		this.category = incomeCategory;
	}


	
	
	
	
	
}
