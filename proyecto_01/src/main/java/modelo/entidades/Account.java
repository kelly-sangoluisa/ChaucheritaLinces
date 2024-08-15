package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cuenta")

public class Account implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCuenta")
	private int idAccount;
	
	@Column(name="nombreCuenta")
	private String nameAccount;
	@Column(name= "total")
	private double total;
	
	@ManyToOne
	@JoinColumn(name = "propietario")
	private User owner;
	
	private static List<Account> accounts =  null;
	
	public Account() {
		
	}


	public Account(int id, String nombre, double total,User user) {
		super();
		this.idAccount = id;
		this.nameAccount = nombre;
		this.total = total;
		this.owner = user;
	}


	public int getIdAccount() {
		return idAccount;
	}


	public void setIdAccount(int id) {
		this.idAccount = id;
	}


	public String getNameAccount() {
		return nameAccount;
	}


	public void setNombreCuenta(String nombre) {
		this.nameAccount = nombre;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}
	
	public User getUser() {
		return owner;
	}


	public void setUser(User user) {
		this.owner = user;
	}


	/*******************METHODOS DE NEGOCIO****************/
	


	
}
