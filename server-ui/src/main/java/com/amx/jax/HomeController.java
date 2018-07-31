




package com.amx.jax;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@GetMapping("/dis")
	public String home()
	{
		return "discoverherein";
		//return "index";
	}
}
