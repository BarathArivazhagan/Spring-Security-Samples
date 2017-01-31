package com.barath.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	
	@GetMapping("/insecured")
	public String unprotectedReosurce(){
		return "you can view this "; 
	}
	
	@GetMapping("/secured")
	public String protectedReosurce(){
		return "Secured Resource "; 
	}


}
