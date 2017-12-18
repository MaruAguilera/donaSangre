package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import model.Pedido;

@Controller
public class PrimerController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	//rutas de tempplates 
	@GetMapping("/index")
	public static String paginaPrincipal(Model template) {
		
		return "index";
	}

	@GetMapping("/info")
	public static String paginaInfo(Model template) {
	
		return "info";
	}

	@GetMapping("/ingresa")
	public static String paginaIngresa(Model template) {
		
		return "ingresa";
	}

	@GetMapping("/contact")
	public static String PaginaContacto(Model template) {
		return "contact"; // Formulario vacio
	}

	//pedido a la base de datos
	@GetMapping("/donar")
	public static String layout(Model template) throws SQLException {
		// se define a donde hay q conectarse
		Connection connection;
		connection = DriverManager.getConnection(settings.db_url, settings.db_user, settings.db_password);
		// se prepara la conecxion
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM infopedido ;");
		// se ejecuta la conexion hacia la base de datos, este resultado queda en una
		// variable de Resulset
		ResultSet resultado = ps.executeQuery();

		ArrayList<Pedido> listaPedido;
		listaPedido = new ArrayList<Pedido>();
		// se imprime con el while, para imprimir la cantidad de sabores q hay...

		while (resultado.next()) {
			// para traer el dato, se pide por GET... como es un string, se pide getSring

			Pedido infoPedido = new Pedido(resultado.getInt("id"), resultado.getString("tipo"),
					resultado.getString("factor"), resultado.getString("hospital"), resultado.getString("receptor"));

			listaPedido.add(infoPedido);

		}
		template.addAttribute("listaPedido", listaPedido);
		return "donar";
	}

	//se introducen datos a la base de datos, por formulario POST
	@PostMapping("/recibirContacto")
	public static String procesarInfoContacto(@RequestParam String nombre, @RequestParam String comentario,
			@RequestParam String email, @RequestParam String asunto, Model template) throws SQLException {
		if (nombre.equals("") || comentario.equals("") || email.equals("") || (asunto.equals(""))) { // si hubo algun
																										// error
			// Cargar formulario de vuelta

			template.addAttribute("nombre1", nombre);
			template.addAttribute("email1", email);
			template.addAttribute("comentario1", comentario);
			template.addAttribute("asunto1", asunto);

			return "contact"; // Formulario vacio (quizas se enoje, pero bue)
		} else {
			enviarCorreo("no-responder@pepito.com", "marisita_87@hotmail.com", "Mensaje de contacto de " + nombre,
					"nombre: " + nombre + "  email: " + email + " comentario: " + comentario + "asunto" + asunto);
			enviarCorreo("no-responder@pepito.com", email, "Gracias por contactarte!",
					"Recibimos tu conulta, nos vamos a contactar con vos");

			Connection connection;
			connection = DriverManager.getConnection(settings.db_url, settings.db_user, settings.db_password);

			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO contactos(nombre, email, comentario, asunto) VALUES(?,?,?,?);");
			ps.setString(1, nombre);
			ps.setString(2, email);
			ps.setString(3, comentario);
			ps.setString(4, asunto);

			ps.executeUpdate();

			return "graciasContacto";
		}
	}

	public static void enviarCorreo(String de, String para, String asunto, String contenido) {
		Email from = new Email(de);
		String subject = asunto;
		Email to = new Email(para);
		Content content = new Content("text/plain", contenido);
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid("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");
		Request request = new Request();
		try {
			request.method = Method.POST;
			request.endpoint = "mail/send";
			request.body = mail.build();
			Response response = sg.api(request);
			System.out.println(response.statusCode);
			System.out.println(response.body);
			System.out.println(response.headers);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			;
		}
	}

	@PostMapping("/recibirPedido")
	public static String ingresarPedido(@RequestParam String tipo, @RequestParam String factor,
			@RequestParam String hospital, @RequestParam String receptor, Model template) throws SQLException {
		if (tipo.equals("Elija el tipo de sangre") || factor.equals("Elija el factor")
				|| hospital.equals("Elija un hospital de la lista") || receptor.isEmpty()) { // si hubo algun error
			// Cargar formulario

			template.addAttribute("tipo1", tipo);
			template.addAttribute("factor1", factor);
			template.addAttribute("hospital1", hospital);
			template.addAttribute("receptor1", receptor);

			return "ingresa"; // Formulario vacio
		} else {

			Connection connection;
			connection = DriverManager.getConnection(settings.db_url, settings.db_user, settings.db_password);

			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO infopedido(tipo, factor, hospital, receptor) VALUES(?,?,?,?);");
			ps.setString(1, tipo);
			ps.setString(2, factor);
			ps.setString(3, hospital);
			ps.setString(4, receptor);

			ps.executeUpdate();

			return "graciasPedido";
		}

	}

	@PostMapping("/donarBusqueda")
	public static String donar(@RequestParam String busquedaHospital, Model template) throws SQLException {

		if (busquedaHospital.equals("Elija un hospital de la lista")) {

			template.addAttribute("busquedaHospital1", busquedaHospital);
			return "donar";
		}

		else {
			Connection connection;
			connection = DriverManager.getConnection(settings.db_url, settings.db_user, settings.db_password);

			PreparedStatement ps = connection.prepareStatement("SELECT * FROM infopedido WHERE hospital LIKE ? ;");
			ps.setString(1, busquedaHospital);
			ResultSet resultado = ps.executeQuery();

			ArrayList<Pedido> listaPedido;
			listaPedido = new ArrayList<Pedido>();

			while (resultado.next()) {

				Pedido infoPedido = new Pedido(resultado.getInt("id"), resultado.getString("tipo"),
						resultado.getString("factor"), resultado.getString("hospital"),
						resultado.getString("receptor"));

				listaPedido.add(infoPedido);

			}
			template.addAttribute("listaPedido", listaPedido);
			return "donar";
		}
	}

}