package modelo.entidades;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TRANSFERENCIA")
public class Transference extends Movement {

	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(name= "origen")
	private Account origin;
	@ManyToOne
	@JoinColumn(name= "destino")
	private Account destiny;
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private TransferenceCategory category;

	public Transference() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transference(int idMovement, String concept, Timestamp date, double amount, TransferenceCategory transferenceCategory,  Account originAccount, Account destinyAccount) {
		super(idMovement, concept, date, amount);
		this.category = transferenceCategory;
		this.origin = originAccount;
		this.destiny =  destinyAccount;
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public Account getOrigin() {
		return origin;
	}

	public void setOrigin(Account origin) {
		this.origin = origin;
	}

	public Account getDestiny() {
		return destiny;
	}

	public void setDestiny(Account destiny) {
		this.destiny = destiny;
	}

	public TransferenceCategory getCategory() {
		return category;
	}

	public void setCategory(TransferenceCategory transferenceCategory) {
		this.category = transferenceCategory;
	}

	
	

	
}
