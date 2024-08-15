package modelo.entidades;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name= "Usuario")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idUser;
	
	@Column(name = "nombre")
	private String userName;
	
	@Column(name ="clave")
	private String password;
	
	public User() {

	}
	public User(int idUser, String name, String password) {
		super();
		this.idUser = idUser;
		this.userName = name;
		this.password = password;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUsuario) {
		this.idUser = idUsuario;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
