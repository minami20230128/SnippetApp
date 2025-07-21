package com.example.snippet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.snippet.repository.SnippetRepository;

@Controller
public class SnippetController {
	private final SnippetRepository snippetRepository;
	
	public SnippetController(SnippetRepository snippetRepository) {
		this.snippetRepository = snippetRepository;
	}
	
	@GetMapping("/")
	public String index(Model model) {
		var snippets = this.snippetRepository.findAll();
		model.addAttribute("snippets", snippets);
		return "index";
	}
}
