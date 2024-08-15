package modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name= "Categoria")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "categoria_type")
public class Category implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDCATEGORIA")
	private int idCategory;
	@Column(name = "nombre")
	private String nameCategory;
	
	public Category() {
		
	}
	public Category(int idCategoria, String nombre) {
		super();
		this.idCategory = idCategoria;
		this.nameCategory = nombre;
	}
	public int getIdCategory() {
		return idCategory;
	}
	public void setIdCategory(int idCategoria) {
		this.idCategory = idCategoria;
	}
	public String getNameCategory() {
		return nameCategory;
	}
	public void setNameCategory(String nombre) {
		this.nameCategory = nombre;
	}
	
	
}
