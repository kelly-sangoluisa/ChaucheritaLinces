package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;




@Entity
@Table(name = "Movimiento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")

public class Movement implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMovimiento")
	private int idMovement;
	
	@Column(name="concepto")
	private String concept;
	@Column(name="fecha")
	private Date date;
	@Column(name="monto")
	private double amount;
	
	
	
	
	
	public Movement() {

		
	}
	
	public Movement(int idMovement, String concept, Date date, double amount) {
		super();
		this.idMovement = idMovement;
		this.concept = concept;
		this.date = date;
		this.amount = amount;

	}


	public int getIdMovement() {
		return idMovement;
	}
	public void setIdMovement(int idMovement) {
		this.idMovement = idMovement;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date fecha) {
		this.date = fecha;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

	
	
}
