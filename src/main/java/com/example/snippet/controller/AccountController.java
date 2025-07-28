package com.example.snippet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class AccountController {
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@GetMapping("/signup")
	public String showSignupForm() {
		return "signup";
	}
}
