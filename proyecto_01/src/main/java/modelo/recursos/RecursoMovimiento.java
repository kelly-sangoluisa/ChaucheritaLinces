package modelo.recursos;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import modelo.dao.OutcomeDAO;
import modelo.dao.IncomeDAO;
import modelo.dao.TransferenceDAO;
import modelo.entidades.Outcome;
import modelo.entidades.Income;
import modelo.entidades.Movement;
import modelo.entidades.Transference;

@Path("/movimientos")
public class RecursoMovimiento {
	IncomeDAO ingreso = new IncomeDAO();
	OutcomeDAO egreso = new OutcomeDAO();
	TransferenceDAO transferencia = new TransferenceDAO();
			
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movement> getMovimientos(){
		List<Movement> movements = new ArrayList<>();
		System.out.println("getMovimientos() method called");
		 List<Income> incomes = ingreso.getAllMovementsIncome();
		    movements.addAll(incomes);

		    // Obtener todos los egresos
		    List<Outcome> outcomes = egreso.getAllMovementsEgreso();
		    movements.addAll(outcomes);

		    // Obtener todas las transferencias
		    List<Transference> transferences = transferencia.getAllMovementsTransference();
		    movements.addAll(transferences);
		    
		return movements;
		
	} 
}
