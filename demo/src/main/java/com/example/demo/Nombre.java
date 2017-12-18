package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Nombre {
	
	@GetMapping("/hola/{nombre}")
	public static String NombreYes(@PathVariable String nombre, Model template) {
		System.out.println("este es el nombre ingresado: " + nombre );
		template.addAttribute("persona", nombre);
		return "bienvenida";
	}

}


//CREAR UNA RUTA QUE TOME NOMBRE Y APELLIDO Y SALUDE CON NOMBRE Y APELLIDO
// "/hola/{nombre}/{apellido}"


//CREAR UNA RUTA "/sortear", que no tome parametros y devuelva en pantalla un numero aleatorio entre 0 y 100

