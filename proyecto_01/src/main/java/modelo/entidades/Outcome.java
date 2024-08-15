package modelo.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("EGRESO")
public class Outcome extends Movement {
	/**
	 * 
	 */
	
	@ManyToOne
	@JoinColumn(name= "origen")
	private Account origin;
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private OutcomeCategory category;
	
	
	

	public Account getOrigin() {
		return origin;
	}

	public void setOrigin(Account origen) {
		this.origin = origen;
	}

	public Outcome() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Outcome(int idMovement, String concept, Date date, double amount, OutcomeCategory outcomeCategory, Account originAccount) {
		super(idMovement, concept, date, amount);
		this.category = outcomeCategory;
		this.origin = originAccount;
		// TODO Auto-generated constructor stub
	}



	public OutcomeCategory getCategoria() {
		return category;
	}

	public void setCategoria(OutcomeCategory outcomeCategory) {
		this.category = outcomeCategory;
	}


	
	
	
	
	
	////////////////////////////
	
	
}
