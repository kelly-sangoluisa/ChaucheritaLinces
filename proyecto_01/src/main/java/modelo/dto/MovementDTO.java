package modelo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.dao.MovementDAO;
import modelo.entidades.Category;
import modelo.entidades.OutcomeCategory;
import modelo.entidades.Account;
import modelo.entidades.Outcome;
import modelo.entidades.Income;
import modelo.entidades.Movement;

public class MovementDTO {
    private int idMovement;
    private String concept;
    private Date date;
    private Double amount;
    private String category;
    private String origin;
    private String destiny;
    public MovementDTO() {
    	
    }
    // Constructor
    public MovementDTO(int idMovement, String concept, Date date, Double amount, 
                         String category, String origin, String destiny) {
        this.idMovement = idMovement;
        this.concept = concept;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.origin = origin;
        this.destiny = destiny;
    }

    // Getters y Setters
    public int getIdMovement() {
        return idMovement;
    }

    public void setIdMovement(int idMovimiento) {
        this.idMovement = idMovimiento;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double monto) {
        this.amount = monto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }
    
   

}

