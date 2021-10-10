package com.bookTracker.BookTracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 
 * This class is meant to simulate a frontend application, this implies that the class
 * is used to serve pages to the user while also communicating with the backend
 * endpoints.
 * @author Georgios Davakos
 * @since 2021-10-10  
 */
@Controller
public class ViewController {

	@GetMapping("/")
	public String root(Model model) {
		
		return "index";
	}
}
