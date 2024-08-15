package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import java.text.ParseException;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.entidades.Category;
import modelo.entidades.OutcomeCategory;
import modelo.entidades.IncomeCategory;
import modelo.entidades.TransferenceCategory;
import modelo.entidades.Account;
import modelo.entidades.Outcome;
import modelo.entidades.Income;
import modelo.entidades.Movement;
import modelo.entidades.Transference;
import modelo.entidades.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import modelo.dao.CategoryDAO;
import modelo.dao.OutcomeCategoryDAO;
import modelo.dao.IncomeCategoryDAO;
import modelo.dao.TransferenceCategoryDAO;
import modelo.dao.AccountDAO;
import modelo.dao.OutcomeDAO;
import modelo.dao.IncomeDAO;
import modelo.dao.MovementDAO;
import modelo.dao.TransferenceDAO;
import modelo.dao.UserDAO;
import modelo.dto.MovementDTO;
@WebServlet("/ContabilidadController")

public class AccountingController extends HttpServlet {

	/**
	 * 
	 */
	private AccountDAO accountDAO;
	private UserDAO UserDAO;
	private CategoryDAO categoryDAO;
	private MovementDAO movementDAO;
	
	private IncomeCategoryDAO incomeCategoryDAO;
	private OutcomeCategoryDAO outcomeCategoryDAO;
	private TransferenceCategoryDAO transferenceCategoryDAO;
	
	private OutcomeDAO outcomeDAO;
	private IncomeDAO incomeDAO;
	private TransferenceDAO transferenceDAO;
	private MovementDTO movementDTO;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		UserDAO = new UserDAO();
		accountDAO = new AccountDAO();
		categoryDAO = new CategoryDAO();
		movementDAO = new MovementDAO();
		movementDTO = new MovementDTO();
		
		incomeCategoryDAO = new IncomeCategoryDAO();
		outcomeCategoryDAO =  new OutcomeCategoryDAO();
		transferenceCategoryDAO = new TransferenceCategoryDAO();
		
		outcomeDAO = new OutcomeDAO();
		incomeDAO = new IncomeDAO();
		transferenceDAO = new TransferenceDAO();
		super.init(config);
	}
	
	public void authenticate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*1.-Obtener los parametros
		 *2.- Hablar con el modelo
		 *3.- Llamar a la vista
		 */
		resp.sendRedirect("jsp/login.jsp");
	}
	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	    String username = req.getParameter("usuario");
	    String password = req.getParameter("clave");
	    User user = UserDAO.authenticate(username, password);

	  
	    if (user != null) {
	    	HttpSession session = req.getSession(true);
	    	
	    	LocalDate hoy = LocalDate.now();
	    	LocalDate primerDiaDelMes = hoy.withDayOfMonth(1);

	    	// Generar LocalDateTime para el inicio y fin del periodo
	    	LocalDateTime fechaInicio = primerDiaDelMes.atStartOfDay();
	    	LocalDateTime fechaFin = hoy.atTime(23, 59, 59);

	    	// Formatear fechas para que sean compatibles con el input datetime-local
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    	String fechaInicioStr = fechaInicio.format(formatter);
	    	String fechaFinStr = fechaFin.format(formatter);
	        
	        // Guardar fechas en la sesión
	        session.setAttribute("fechaInicio", fechaInicioStr);
	        session.setAttribute("fechaFin", fechaFinStr);
	        session.setAttribute("currentDate", fechaFinStr);
	    	req.getSession().setAttribute("usuarioId", user.getIdUser()); 
	    	session.setMaxInactiveInterval(30 * 60);
	        resp.sendRedirect("ContabilidadController?ruta=showdashboard");
	        
	    } else {
	        // Si la autenticación falla, redirigir a la página de inicio de sesión con un mensaje de error
	        req.getSession().setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos");
	        resp.sendRedirect("jsp/login.jsp"); 
	    }
		
		
	}
	public void renderCreateUserForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.sendRedirect("jsp/createUser.jsp");
	}
	public void createUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nameUser =  req.getParameter("nombre");
		String password =  req.getParameter("clave");
		User user  =  new User(0,nameUser,password);
		TransferenceCategory existente = transferenceCategoryDAO.getCategoriyTransferenceByName("Transferencia entre Cuentas");
		if (existente == null) {
	        // La categoría no existe, así que la guardamos
			Category category = new TransferenceCategory(0,"Transferencia entre Cuentas");
			transferenceCategoryDAO.saveCategoriaTransferencia((TransferenceCategory) category);
	    }
		
		UserDAO.createUser(user);
		resp.sendRedirect("ContabilidadController?ruta=authenticate");
	}
	
	
	public void viewDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idUser =(int) req.getSession().getAttribute("usuarioId");
		HttpSession session = req.getSession();
		
		

	    // Leer fechas desde la solicitud
	    String fechaInicioStr = req.getParameter("fechaInicio");
	    String fechaFinStr = req.getParameter("fechaFin");

	    // Usar formato para fechas
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	    LocalDateTime fechaInicio;
	    LocalDateTime fechaFin;

	    if (fechaInicioStr != null && !fechaInicioStr.isEmpty() &&
	        fechaFinStr != null && !fechaFinStr.isEmpty()) {
	        // Si se proporcionan nuevas fechas, usarlas
	        fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
	        fechaFin = LocalDateTime.parse(fechaFinStr, formatter);
	    } else {
	        // Si no se proporcionan fechas, usar las fechas almacenadas en la sesión
	        String fechaInicioSessionStr = (String) session.getAttribute("fechaInicio");
	        String fechaFinSessionStr = (String) session.getAttribute("fechaFin");

	        if (fechaInicioSessionStr != null && !fechaInicioSessionStr.isEmpty() &&
	            fechaFinSessionStr != null && !fechaFinSessionStr.isEmpty()) {
	            // Convertir las fechas de la sesión a LocalDateTime
	            fechaInicio = LocalDateTime.parse(fechaInicioSessionStr, formatter);
	            fechaFin = LocalDateTime.parse(fechaFinSessionStr, formatter);
	        } else {
	            // Establecer fechas predeterminadas si no hay fechas en la sesión
	            fechaInicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	            fechaFin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
	        }
	    }

	    // Guardar las fechas en la sesión
	    session.setAttribute("fechaInicio", fechaInicio.format(formatter));
	    session.setAttribute("fechaFin", fechaFin.format(formatter));

	    Timestamp startDate = Timestamp.valueOf(fechaInicio);
	    Timestamp endDate = Timestamp.valueOf(fechaFin);
	    
	    
	    
	    
		List<Account> accounts = accountDAO.getAllAccountsByUserId(idUser);
		List<Category> categories = categoryDAO.findAll();
		
		
		
		    
		List<IncomeCategory> ingresos = new ArrayList<>();
	    List<OutcomeCategory> egresos = new ArrayList<>();
	    List<TransferenceCategory> transferencias = new ArrayList<>();
	    
	    List<Movement> movements = movementDAO.getAllMovementsByUserId(idUser,startDate,endDate);
	    List<MovementDTO> movimientosDTO = movementDAO.getAllMovementsDTO(movements);
	    
	    Map<String, Double> ingresosTotales = incomeCategoryDAO.getAllSumarizedByUserId(idUser,startDate,endDate);
	    Map<String, Double> totalEgresos = outcomeCategoryDAO.getAllSumarizedByUserId(idUser,startDate,endDate);
	    Map<String, Double> transferenciasTotales = transferenceCategoryDAO.getAllSumarizedByUserId(idUser,startDate,endDate);


	    for (Category category : categories) {
	        if (category instanceof IncomeCategory) {
	            ingresos.add((IncomeCategory) category);
	        } else if (category instanceof OutcomeCategory) {
	            egresos.add((OutcomeCategory) category);
	        } else if (category instanceof TransferenceCategory) {
	            transferencias.add((TransferenceCategory) category);
	        }
	    }
	    System.out.println("Número de cuentas: " + accounts.size());
	    req.setAttribute("origen", "showdashboard");
        req.setAttribute("cuentas", accounts);
        req.setAttribute("ingresos", ingresos);
        req.setAttribute("egresos", egresos);
        req.setAttribute("transferencias", transferencias);
        req.setAttribute("movimientos", movimientosDTO);
        req.setAttribute("totalIngresos", ingresosTotales);
        req.setAttribute("totalEgresos", totalEgresos);
        req.setAttribute("totalTransferencias", transferenciasTotales);
        
        req.getRequestDispatcher("jsp/viewDashboard.jsp").forward(req, resp);
		
	}
	public void filterDate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		
		String origen = req.getParameter("origen");
	
		
	    // Leer fechas desde la solicitud
	    String fechaInicioStr = req.getParameter("fechaInicio");
	    String fechaFinStr = req.getParameter("fechaFin");

	    // Usar formato para fechas
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	    LocalDateTime startDate;
	    LocalDateTime endDate;

	    if (fechaInicioStr != null && !fechaInicioStr.isEmpty() &&
	        fechaFinStr != null && !fechaFinStr.isEmpty()) {
	        // Si se proporcionan nuevas fechas, usarlas
	        startDate = LocalDateTime.parse(fechaInicioStr, formatter);
	        endDate = LocalDateTime.parse(fechaFinStr, formatter);
	    } else {
	        // Si no se proporcionan fechas, usar las fechas almacenadas en la sesión
	        String fechaInicioSessionStr = (String) session.getAttribute("fechaInicio");
	        String fechaFinSessionStr = (String) session.getAttribute("fechaFin");

	        if (fechaInicioSessionStr != null && !fechaInicioSessionStr.isEmpty() &&
	            fechaFinSessionStr != null && !fechaFinSessionStr.isEmpty()) {
	            // Convertir las fechas de la sesión a LocalDateTime
	            startDate = LocalDateTime.parse(fechaInicioSessionStr, formatter);
	            endDate = LocalDateTime.parse(fechaFinSessionStr, formatter);
	        } else {
	            // Establecer fechas predeterminadas si no hay fechas en la sesión
	            startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	            endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
	        }
	    }

	    // Guardar las fechas en la sesión
	    session.setAttribute("fechaInicio", startDate.format(formatter));
	    session.setAttribute("fechaFin", endDate.format(formatter));

	    
	    if ("showdashboard".equals(origen)) {
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen);
	    }else if ("showaccount".equals(origen)) {
	    	int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&cuentaId="+ cuentaId);
	    	
	    }else if ("showcategory".equals(origen)) {
	    	int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&categoriaId="+ categoriaId);
	    }
	    
	    
	    
	    
	}
	public void viewAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // Obtener el identificador de la cuenta desde la solicitud
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
		HttpSession session = req.getSession();

	    // Obtener fechas desde la sesión
	    String fechaInicioStr = (String) session.getAttribute("fechaInicio");
	    String fechaFinStr = (String) session.getAttribute("fechaFin");

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    LocalDateTime fechaInicio = fechaInicioStr != null && !fechaInicioStr.isEmpty() ?
	        LocalDateTime.parse(fechaInicioStr, formatter) :
	        LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	    LocalDateTime fechaFin = fechaFinStr != null && !fechaFinStr.isEmpty() ?
	        LocalDateTime.parse(fechaFinStr, formatter) :
	        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

	    // Convertir a Timestamp para su uso en la base de datos
	    Timestamp startDate = Timestamp.valueOf(fechaInicio);
	    Timestamp endDate  = Timestamp.valueOf(fechaFin);

    
	    // Obtener la cuenta y sus movimientos desde el DAO
	    Account account = accountDAO.getAccountById(idAccount);
	    List<Movement> allMovements = movementDAO.getMovementsByAccount(idAccount);


	    List<MovementDTO> movementsDTO= movementDAO.getAllMovementsDTO(allMovements);
	    
	    
	    
	    
	    // Establecer atributos en la solicitud
	    req.setAttribute("origen", "showaccount");
	    req.setAttribute("cuenta", account);
	    req.setAttribute("movimientos", movementsDTO);
	    
	    // Redirigir a la vista verCuenta.jsp
	    req.getRequestDispatcher("jsp/viewAccount.jsp").forward(req, resp);
	}

	
	public void viewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int idUser =(int) req.getSession().getAttribute("usuarioId"); 
	    int idCategory = Integer.parseInt(req.getParameter("categoriaId"));
	    
	    HttpSession session = req.getSession();

	    // Obtener fechas desde la sesión
	    String fechaInicioStr = (String) session.getAttribute("fechaInicio");
	    String fechaFinStr = (String) session.getAttribute("fechaFin");

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    LocalDateTime fechaInicio = fechaInicioStr != null && !fechaInicioStr.isEmpty() ?
	        LocalDateTime.parse(fechaInicioStr, formatter) :
	        LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	    LocalDateTime fechaFin = fechaFinStr != null && !fechaFinStr.isEmpty() ?
	        LocalDateTime.parse(fechaFinStr, formatter) :
	        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

	    // Convertir a Timestamp para su uso en la base de datos
	    Timestamp startDate = Timestamp.valueOf(fechaInicio);
	    Timestamp endDate = Timestamp.valueOf(fechaFin);
	    

		 Category category = categoryDAO.findCategoryById(idCategory);
		 //List<Movimiento> movimientos;
		 List<Movement> movimientosEgreso = null;
		 List<Movement> movimientosIngreso = null;
		 List<Movement> movimientosTransferencia = null;
		 double total = 0.0;
		 List<MovementDTO> movimientosDTO = null;
		    if (category instanceof IncomeCategory) {
		        
		    	movimientosIngreso = incomeDAO.findMovementsByCategoryIncome(idCategory,idUser, startDate,endDate);
		    	
		    	movimientosDTO = movementDAO.getAllMovementsDTO(movimientosIngreso);
		    	total = incomeCategoryDAO.getSumByUserIdAndCategory(idUser, idCategory , startDate,endDate);
		    	req.setAttribute("movimientos", movimientosDTO);
		    } else if (category instanceof OutcomeCategory) {
		    	movimientosEgreso = outcomeDAO.findMovementsByOutcomeCategory(idCategory, idUser , startDate,endDate);
		    	movimientosDTO = movementDAO.getAllMovementsDTO(movimientosEgreso);
		    	
		    	
		    	total = outcomeCategoryDAO.getSumByUserIdAndCategory(idUser,idCategory, startDate,endDate);
		    	
		    	req.setAttribute("movimientos", movimientosDTO);
		    	
		    } else if (category instanceof TransferenceCategory) {
		    	movimientosTransferencia = transferenceDAO.findMovementsByCategoryTransference(idCategory,idUser , startDate,endDate);
		    	movimientosDTO = movementDAO.getAllMovementsDTO(movimientosTransferencia);
		        total = transferenceCategoryDAO.getSumByUserIdAndCategory(idUser,idCategory, startDate,endDate);
		        req.setAttribute("movimientos", movimientosDTO);
		    }
		    
		    req.setAttribute("origen", "showcategory");
		    req.setAttribute("categoria", category);
		    req.setAttribute("total", total);
		    
		    
		    req.getRequestDispatcher("jsp/viewCategory.jsp").forward(req, resp);
	}
	
	public void doIncome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
		String origen =req.getParameter("origen");
		if (origen == null || origen.isEmpty()) {
	        origen = "showaccount"; // Valor predeterminado
	    }
		Account account = accountDAO.getAccountById(idAccount);
		List<Category> incomeCategories = incomeCategoryDAO.getCategoriesIncome();
		
		req.setAttribute("origen", origen);
		req.setAttribute("cuenta", account);
		req.setAttribute("categorias", incomeCategories);
		req.getRequestDispatcher("jsp/registerIncome.jsp").forward(req, resp);
		
	
	
	}
	
	
	
	
	
	public void doOutcome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
		 String origen = req.getParameter("origen");
		 if (origen == null || origen.isEmpty()) {
		        origen = "showaccount"; // Valor predeterminado
		    }
		Account account = accountDAO.getAccountById(idAccount);
		List<Category> outcomeCategories = outcomeCategoryDAO.getCategoriesOutcome();
		req.setAttribute("origen", origen);
		req.setAttribute("cuenta", account);
		req.setAttribute("categorias", outcomeCategories);
		req.getRequestDispatcher("jsp/registerOutcome.jsp").forward(req, resp);
	}
	
	public void outcome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
        String concept = req.getParameter("concepto");
        double amount = Double.parseDouble(req.getParameter("monto"));
        String fechaStr = req.getParameter("fecha");
        int idCategory = Integer.parseInt(req.getParameter("categoria"));
        Account account = accountDAO.getAccountById(idAccount);
        Category category  = outcomeCategoryDAO.getCategoryById(idCategory);
        String origen = req.getParameter("origen");
        
        Timestamp date = convertDate(fechaStr,resp);

	    amount = -amount;
	    try{
	    	
		    //movimiento
		    Outcome newOutcome=  new Outcome(0,concept,date,amount,(OutcomeCategory) category,account);
		    outcomeDAO.createOutcome(newOutcome, accountDAO, account);// Enviar  accontDao para actualizar el movimiento.
		    
		    
	        //actualiza la cuenta
		    if("showdashboard".equals(origen)) {
		    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&mensaje=Egreso Registrado exitosamente");
		    }else{
		    	resp.sendRedirect("ContabilidadController?ruta=showaccount&cuentaId=" + idAccount + "&mensaje= Egreso registrado exitosamente");
		    }
	    }catch(Exception e) {
	    	req.setAttribute("errorMessage", "Error al registrar el egreso: " + e.getMessage());
	    	req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
	    }
	    
	   
        
        
		
	}
	
	
	
	
	
	
	
	public void income(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
        String concept = req.getParameter("concepto");
        double amount = Double.parseDouble(req.getParameter("monto"));
        String fechaStr = req.getParameter("fecha");
        int idCategory = Integer.parseInt(req.getParameter("categoria"));
        String origen = req.getParameter("origen");
        Account account = accountDAO.getAccountById(idAccount);
        
        Category category = incomeCategoryDAO.getCategoryById(idCategory);

        Timestamp date = convertDate(fechaStr,resp);
        
        
        try {
        	
    	    Income newIncome =  new Income(0,concept,date,amount,(IncomeCategory) category,account);
    	    incomeDAO.createIncome(newIncome);
            
    	    accountDAO.updateTotal(idAccount, amount);
    	 
    	    if( "showdashboard".equals(origen)) {
    	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&mensaje=Ingreso Registrado exitosamente");
    	    }else
    	    {
    	    	resp.sendRedirect("ContabilidadController?ruta=showaccount&cuentaId=" + idAccount + "&mensaje=Ingreso registrado exitosamente");
    	    }
        	
        	
        }catch(Exception e) {
        	req.setAttribute("errorMessage", "Error al registrar la transferencia: " + e.getMessage());
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
 	   }
	    
        
        
	
	}
	
	public void doTransference(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idAccount = Integer.parseInt(req.getParameter("cuentaId"));
		int idUser =(int) req.getSession().getAttribute("usuarioId"); 
		String origen = req.getParameter("origen");
		if (origen == null || origen.isEmpty()) {
	        origen = "showaccount"; // Valor predeterminado
	    }
	    // Obtener la cuenta de origen
	    Account originAccount = accountDAO.getAccountById(idAccount);
	    
	    // Obtener todas las cuentas excepto la cuenta de origen
	    List<Account> allAccounts = accountDAO.getAllAccountsByUserId(idUser);
	    List<Account> destinyAccounts = allAccounts.stream()
	        .filter(cuenta -> cuenta.getIdAccount() != idAccount)
	        .collect(Collectors.toList());
	    List<Category> categories = transferenceCategoryDAO.getCategoryTransference();
	    
	    req.setAttribute("origen", origen);
	    req.setAttribute("cuenta", originAccount);
	    req.setAttribute("cuentasDestino", destinyAccounts);
	    req.setAttribute("categorias", categories);
	    
	    
	    
	    req.getRequestDispatcher("jsp/registerTransference.jsp").forward(req, resp);
	    
	}
	
	public void transference(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idOriginAccount = Integer.parseInt(req.getParameter("cuentaOrigenId"));
		
		System.out.println(req.getParameter("cuentaOrigenId"));
		System.out.println(idOriginAccount);
		int idDestinyAccount = Integer.parseInt(req.getParameter("destino"));
		System.out.println(req.getParameter("destino"));
		System.out.println(idDestinyAccount);
        String concept = req.getParameter("concepto");
        
        double amount = Double.parseDouble(req.getParameter("monto"));
        
        String fechaStr = req.getParameter("fecha");
        
        int idCategory = Integer.parseInt(req.getParameter("categoria"));
        
        Account originAccount = accountDAO.getAccountById(idOriginAccount);
        Account  destinyAccount = accountDAO.getAccountById(idDestinyAccount);
        String origin = req.getParameter("origen");
        Category category  = transferenceCategoryDAO.getCategoryById(idCategory);
        
        Timestamp date = convertDate(fechaStr,resp);

        try {
        	
        	
            
            
    	    
    	    Transference newTransference=  new Transference(0,concept,date,amount,(TransferenceCategory) category,originAccount,destinyAccount);
    	    														//int idMovimiento, String concepto, Date fecha, double valor, CategoriaTransferencia categoriaTransferencia,  Cuenta origenCuenta, Cuenta destinoCuenta
    	    transferenceDAO.createTransference(newTransference);
    	    
    	    accountDAO.updateTotal(idOriginAccount, -amount);
    	    accountDAO.updateTotal(idDestinyAccount, amount);
    	    
    	    if("showdashboard".equals(origin)) {
    	    	resp.sendRedirect("ContabilidadController?ruta=" + origin + "&mensaje=Transferencia Registrada exitosamente");
    	    }else
    	    {
    	    	resp.sendRedirect("ContabilidadController?ruta=showaccount&cuentaId=" + idOriginAccount + "&mensaje=Transferencia registrada exitosamente");
    	    }
        	
        	
        }catch(Exception e) {
        	 req.setAttribute("errorMessage", "Error al registrar la transferencia: " + e.getMessage());
             req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
 	   }

	    
	    
        
        
		
	}
	
	//**//
	
	
	
	
	public void deleteMovement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idMovement =  Integer.parseInt(req.getParameter("idMovimiento"));
		String vistaOrigen = req.getParameter("vistaOrigen");
		
		Movement movement =  movementDAO.findMovementById(idMovement);
		
		try {
			
			
			if (movement instanceof Outcome) {
				 Outcome outcome = (Outcome) movement;
		        // Para un Egreso, el monto debe ser restado
		        
		        movementDAO.deleteMovement(idMovement);
		        accountDAO.updateTotal(outcome.getOrigin().getIdAccount(),-outcome.getAmount());
		        
		    } else if (movement instanceof Income) {
		    	Income income = (Income) movement;
		        // Para un Ingreso, el monto debe ser sumado
		    	
		    	movementDAO.deleteMovement(idMovement);
		    	accountDAO.updateTotal(income.getDestiny().getIdAccount(),-income.getAmount());
		    	
		    } else if (movement instanceof Transference) {
		        Transference transference = (Transference) movement;
		        // Para una Transferencia, ajustar dos cuentas
		        accountDAO.updateTotal(transference.getOrigin().getIdAccount(), transference.getAmount());
		        accountDAO.updateTotal(transference.getDestiny().getIdAccount(),-transference.getAmount());
		        movementDAO.deleteMovement(idMovement);
		    }
			
			
			
			
			 String redireccionURL = "ContabilidadController?ruta=showdashboard"; // Valor por defecto

			    if ("showaccount".equals(vistaOrigen)) {
			        redireccionURL = "ContabilidadController?ruta=showaccount&cuentaId=" + Integer.parseInt(req.getParameter("idCuenta"));
			    } else if ("showcategory".equals(vistaOrigen)) {
			    	System.out.println(req.getParameter("idCategoria"));
			        redireccionURL = "ContabilidadController?ruta=showcategory&categoriaId=" + Integer.parseInt(req.getParameter("idCategoria"));
			    }
			    
			    resp.sendRedirect(redireccionURL);
			
			
		}catch(Exception e) {
			req.setAttribute("errorMessage", "Error al registrar la transferencia: " + e.getMessage());
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
		   }
		
		
	}
	public void doUpdateMovement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int  idMovement = Integer.parseInt(req.getParameter("idMovimiento"));
		Movement movement = movementDAO.findMovementById(idMovement);
		Date date = movement.getDate();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		String fechaFormateada = localDateTime.format(formatter);
		 int idUser = (int) req.getSession().getAttribute("usuarioId");
		
		 List<Category> categories = null;
		 List<Account> destinyAccounts = accountDAO.getAllAccountsByUserId(idUser);
		 req.setAttribute("cuentasDestino", destinyAccounts);
		 req.setAttribute("fechaFormateada", fechaFormateada);
	    // Agregar los datos a la solicitud
	   
	    if (movement instanceof Transference) {
	    	categories = transferenceCategoryDAO.getCategoryTransference();
	    	Integer destinyId = transferenceDAO.getDestinyIdByTransferenceId(idMovement);
	    	Integer originId = transferenceDAO.getOriginIdByTransferenceId(idMovement);
	    	Integer idCategory = transferenceDAO.getCategoryIdTransferenceId(idMovement);

	    	
	    	
	    	req.setAttribute("origenId", originId);
	    	req.setAttribute("destinoId", destinyId);
	    	req.setAttribute("categoriaId", idCategory);
	    	req.setAttribute("movimiento", movement);
		    req.setAttribute("categoria", categories);
		  
	    	req.getRequestDispatcher("jsp/UpdateMovementTransference.jsp").forward(req, resp);
	    } else if (movement instanceof Income) {
	    	Integer idAccount = incomeDAO.getAccountIdByIncomeId(idMovement);
	    	Integer idCategory = incomeDAO.getCategoryIdByIncomeId(idMovement);
	    	categories = incomeCategoryDAO.getCategoriesIncome();
	    	
	    	
	    	req.setAttribute("destinoId", idAccount);
	    	req.setAttribute("categoriaId", idCategory);
	    	req.setAttribute("movimiento", movement);
		    req.setAttribute("categoria", categories);
		   
	    	req.getRequestDispatcher("jsp/UpdateMovementIncome.jsp").forward(req, resp);
	    } else if (movement instanceof Outcome) {
	    	Integer idAccount = outcomeDAO.getAccountIdByOutcomeId(idMovement);
	    	Integer idCategory = outcomeDAO.getCategoryIdByOutcomeId(idMovement);
	    	
	    	categories = outcomeCategoryDAO.getCategoriesOutcome();
	    	req.setAttribute("movimiento", movement);
		    req.setAttribute("categoria", categories);
		    req.setAttribute("origenId", idAccount);
	    	req.setAttribute("categoriaId", idCategory);
	    	req.getRequestDispatcher("jsp/UpdateMovementOutcome.jsp").forward(req, resp);
	    }
	    ;
	    
	}
	public void updateMovement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
	        // Recoger datos del formulario
			 int idMovement = Integer.parseInt(req.getParameter("idMovimiento"));
		        String concept = req.getParameter("concepto");
		        double amount = Double.parseDouble(req.getParameter("monto"));
		        String fechaStr = req.getParameter("fecha");
		        int idCategory = Integer.parseInt(req.getParameter("categoria"));
		        
		        Timestamp date = convertDate(fechaStr, resp);
		        // Obtener los objetos necesarios
		        
		        //Proceso para eliminar el valor anterior de la cuenta 
		        Movement previousMovement = movementDAO.findMovementById(idMovement);
		        double previousAmount = previousMovement.getAmount();
		       
	        

	        // Actualizar los detalles del movimiento

	        // Actualizar en función del tipo de movimiento
	        if (previousMovement instanceof Transference) {
	        	
	        	Transference previousTransference = (Transference) previousMovement;
		        int previousIdAccountOrigin = previousTransference.getOrigin().getIdAccount();
		        int previousIdAccountDestiny = previousTransference.getDestiny().getIdAccount();
		        Account previousAccount = accountDAO.getAccountById(previousIdAccountOrigin);
	        	
	        	double total = previousAmount - amount;
	        	Transference transference = (Transference) previousMovement;
        		int idOrigin = Integer.parseInt(req.getParameter("cuentaOrigen")); 
	            int idDestiny = Integer.parseInt(req.getParameter("cuentaDestino"));  
	            Account origin = accountDAO.getAccountById(idOrigin);
	            Account destiny = accountDAO.getAccountById(idDestiny);
	            Category transferenceCategory= transferenceCategoryDAO.getCategoryById(idCategory);
	        	
	        	if(total >= 0 ){
	        		
		           
		            transference.setOrigin(origin);
		            transference.setDestiny(destiny);
		            transference.setAmount(amount);
		            transference.setCategory((TransferenceCategory)transferenceCategory);
		            accountDAO.updateTotal(idOrigin, total);
		            accountDAO.updateTotal(idDestiny, -total);
		            transferenceDAO.updateTransferencia(transference);
	        	}
	        	else {
	        		double diferencia = origin.getTotal() + total;
	        		if (diferencia <0) {
	        			req.setAttribute("errorMessage", "Error al actualizar el movimiento Transferencia " + "No hay saldo suficiente para actualizar el movimiento");
		    	        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
	        		}
	        		else {
	        			transference.setOrigin(origin);
			            transference.setDestiny(destiny);
			            transference.setAmount(amount);
			            transference.setCategory((TransferenceCategory)transferenceCategory);
			            accountDAO.updateTotal(idOrigin, total);
			            accountDAO.updateTotal(idDestiny, -total);
			            transferenceDAO.updateTransferencia(transference);
	        		}
	        	}
	        	
	        	
	        } else if (previousMovement instanceof Income) {
	        	
	        	//Ingreso Anterior
		        Income previousIncome = (Income) previousMovement;
		        int previousIdAccount = previousIncome.getDestiny().getIdAccount();
		        Account previousAccount = accountDAO.getAccountById(previousIdAccount);
		        int idAccount = Integer.parseInt(req.getParameter("cuentaDestino")); 
	            Account destiny = accountDAO.getAccountById(idAccount);
		        
		        if(amount >= previousAmount) {
		        	Income income = (Income) previousMovement;
		            Category categoryIncome = incomeCategoryDAO.getCategoryById(idCategory);
		            income.setDestiny(destiny);
		            income.setAmount(amount);
		            income.setCategory((IncomeCategory)categoryIncome);
		            accountDAO.updateTotal(idAccount, amount);
		            incomeDAO.updateIncome(income);
		        }else {
		        	double diferencia = previousAmount- amount;
		        	System.out.println("diferencia" + previousAmount +" asdsa" + amount + " "  + diferencia +" Destino" + destiny.getTotal());
		        	double valorCuenta =destiny.getTotal() -diferencia ;
		        	System.out.println("Saldo" + valorCuenta);
		        	if(valorCuenta < 0  ) {
		        		req.setAttribute("errorMessage", "Error al actualizar el movimiento Ingreso " + "No hay saldo suficiente para actualizar el movimiento");
		    	        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
		    	        
		        	}else {
		        		double total = destiny.getTotal() - diferencia;
		        		destiny.setTotal(total);
		        		Income income = (Income) previousMovement;
			            Category categoryIncome = incomeCategoryDAO.getCategoryById(idCategory);
			            income.setDestiny(destiny);
			            income.setCategory((IncomeCategory)categoryIncome);
			            income.setAmount(amount);
			            incomeDAO.updateIncome(income);
		        	}
		        }
		        
	        	
	        	//Ingreso Actualizado
	        	
	        	  
	            
	            
	        } else if (previousMovement instanceof Outcome) {
	        	Outcome previousOutcome = (Outcome) previousMovement;
		        int idOriginAccount = previousOutcome.getOrigin().getIdAccount();
		        Account previousAccount = accountDAO.getAccountById(idOriginAccount);
	        	double total = -previousAmount -  amount;
	        	System.out.println("previous" + previousAmount + "   " + amount);
	        	//double diferencia = total - amount;
	        	
	        	if(total < 0 ){
	        		
	        		double diferencia = previousAccount.getTotal() + total;
	        		if(diferencia< 0 ) {
	        			req.setAttribute("errorMessage", "Error al actualizar el movimiento: " + "No hay saldo suficiente para actualizar el movimiento con un monto mayor");
		    	        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
	        		}else {
	        			Outcome outcome = (Outcome) previousMovement;
			            int idAccount = Integer.parseInt(req.getParameter("cuentaOrigen")); 
			            Account origin = accountDAO.getAccountById(idAccount);
			            Category categoryOutcome = outcomeCategoryDAO.getCategoryById(idCategory);
			            outcome.setAmount(-amount);
			            outcome.setOrigin(origin);
			            outcome.setCategoria((OutcomeCategory)categoryOutcome);
			            accountDAO.updateTotal(idAccount, total);
	        		}
	        		
	        	}
	        	else {
	        		Outcome outcome = (Outcome) previousMovement;
		            int idAccount = Integer.parseInt(req.getParameter("cuentaOrigen")); 
		            Account origin = accountDAO.getAccountById(idAccount);
		            Category categoryOutcome = outcomeCategoryDAO.getCategoryById(idCategory);
		            outcome.setAmount(-amount);
		            outcome.setOrigin(origin);
		            outcome.setCategoria((OutcomeCategory)categoryOutcome);
		            accountDAO.updateTotal(idAccount, total);
		            outcomeDAO.updateOutcome(outcome);
	        	}
		        
	        	
	        	
	        	
	        
	        }

	        // Redirigir a la página de éxito o mostrar un mensaje de éxito
	        resp.sendRedirect("ContabilidadController?ruta=showdashboard&mensaje=Movimiento Modificado exitosamente");
	    } catch (Exception e) {
	        
	        req.setAttribute("errorMessage", "Error al actualizar el movimiento: " + e.getMessage());
	        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
	    }
	}
	public void cerrarSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirigir al usuario a la página de inicio o de confirmación
        resp.sendRedirect("jsp/login.jsp"); 
	}

	private void router(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ruta = (req.getParameter("ruta") == null)? "authenticate": req.getParameter("ruta");
		switch (ruta) {
				case "login": 
					this.login(req, resp);
					break;
				case "authenticate":
					this.authenticate(req, resp);
					break;
				case "showFormUser":
					this.renderCreateUserForm(req,resp);
					break;
					
				case "createUser":
					this.createUser(req,resp);
					break;
				case "showdashboard":
					this.viewDashboard(req, resp);
					break;
				case "showFormAccount":
	                this.doAccount(req, resp);
	                break;
				case "createAccount":
	                this.createAccount(req, resp);
	                break;
				case "eliminarCuenta":
	                this.eliminarCuenta(req, resp);
	                break;
				case "showFormCategory":
	                this.doCategory(req, resp);
	                break;    
				case "createCategory":
	                this.createCategory(req, resp);
	                break;
				case "eliminarCategoria":
					this.deleteCategory(req,resp);
					break;
				case "showaccount":
					this.viewAccount(req, resp);
					break;
				case "showcategory":
					this.viewCategory(req, resp);
					break;
				case "registerFormIncome":
					this.doIncome(req,resp);
					break;
					
				case "registerIncome":
					this.income(req,resp);
					break;
				case "registerFormOutcome":
					this.doOutcome(req,resp);
					break;
					
				case "registerOutcome":
					this.outcome(req,resp);
					break;
				
				case "registerFormTransference":
					this.doTransference(req,resp);
					break;

				case "transfer":
					this.transference(req,resp);
					break;
				case "updateMovement":
					this.updateMovement(req,resp);
					break;
				case "UpdateFormMovement":
					this.doUpdateMovement(req, resp);
					break;
				case "deleteMovement":
					this.deleteMovement(req,resp);
					break;
				case "filter":
					this.filterDate(req, resp);
					break;
				case "cerrarSesion":
					this.cerrarSesion(req,resp);
					break;
					
				default:
					
				}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.router(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.router(req, resp);
	}

	
	
	
	/*Posterior*/
	
	public Timestamp convertDate(String fechaStr, HttpServletResponse resp) throws IOException {
	    if (fechaStr == null || fechaStr.trim().isEmpty()) {
	        // Manejo de error si la fecha es nula o vacía
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha no proporcionada");
	        return null;
	    }

	    Timestamp fecha = null;
	    try {
	        fechaStr = fechaStr.replace("T", " "); // Reemplazar 'T' con un espacio para el formato correcto
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        dateFormat.setLenient(false); // Asegurarse de que el análisis sea estricto
	        Date parsedDate = dateFormat.parse(fechaStr);
	        fecha = new Timestamp(parsedDate.getTime()); // Convertir la fecha del formulario a un objeto Timestamp
	    } catch (ParseException e) {
	        // Manejo de error si la fecha no es válida
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha inválida");
	        return null;
	    }

	    return fecha;
	}
	
	
	
	public void createAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombreCuenta");
        double saldo = Double.parseDouble(req.getParameter("saldo"));
        int usuarioId = (int) req.getSession().getAttribute("usuarioId");
        User user = UserDAO.findUsuarioById(usuarioId);
        
        Account account = new Account(0,nombre,saldo,user);

        accountDAO.createAccount(account);
        resp.sendRedirect("ContabilidadController?ruta=showdashboard");
        //resp.sendRedirect("jsp/createCategoria.jsp");
    }
	//Ignorar 
	public void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String idCuentaParam = req.getParameter("idCuenta");
		    
		    if (idCuentaParam != null) {
		        int idCuenta = Integer.parseInt(idCuentaParam);
		        AccountDAO accountDAO = new AccountDAO();
		        accountDAO.delete(idCuenta);
		        resp.sendRedirect("ContabilidadController?ruta=showdashboard");
		    } else {
		        // Manejo de error si no se proporciona el idCuenta
		        req.getSession().setAttribute("errorMessage", "ID de cuenta no proporcionado.");
		        resp.sendRedirect("jsp/error.jsp");
		    }
	}
	//ignorar
	public void doCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("jsp/createCategory.jsp").forward(req, resp);
	}
	
	
	//ignorar
	public void createCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String tipoCategoria = req.getParameter("tipoCategoria");
	    String nombreCategoria = req.getParameter("nombreCategoria");

	    CategoryDAO categoryDAO = null;
	    Category category = null;

	    if ("egreso".equalsIgnoreCase(tipoCategoria)) {
	        category = new OutcomeCategory(0,nombreCategoria);
	        categoryDAO = new OutcomeCategoryDAO();
	    } else if ("ingreso".equalsIgnoreCase(tipoCategoria)) {
	        category = new IncomeCategory(0,nombreCategoria);
	        categoryDAO = new IncomeCategoryDAO();
	    } 

	    if (category != null && categoryDAO != null) {
	        categoryDAO.saveCategory(category);
	        resp.sendRedirect("ContabilidadController?ruta=showdashboard");
	    } else {
	        // Manejo de error
	    }
	}
	//ignorar
	public void deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int idCategoria = Integer.parseInt(req.getParameter("idCategoria"));
		categoryDAO.deleteCategory(idCategoria);
		resp.sendRedirect("ContabilidadController?ruta=showdashboard");
		
	}
public void doAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int usuarioId = (int) req.getSession().getAttribute("usuarioId");
	    User user = UserDAO.findUsuarioById(usuarioId); // Asegúrate de tener un método para obtener un usuario por ID
	    //int usuarioId =(int) req.getSession().getAttribute("usuarioId");
	    System.out.println("Usuario" + user.getUserName());
	    req.setAttribute("usuario", user);
		req.getRequestDispatcher("jsp/createAccount.jsp").forward(req, resp);
	}
	
	
	
}
