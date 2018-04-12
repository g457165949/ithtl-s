package com.ven.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="about")
public class AboutController {
	
    @ModelAttribute("module")
    String module() {
        return "about";
    }
    
	@RequestMapping(value = {"","/index"})
	public String index() {
		return "about/index";
	}
}
