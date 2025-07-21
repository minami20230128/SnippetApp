package com.example.snippet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.snippet.repository.SnippetRepository;

@Controller
@RequestMapping("/snippets")
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
	
	@GetMapping("/{id}")
	public String detail(@PathVariable int id, Model model) {
	    var snippet = this.snippetRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
		model.addAttribute("snippet", snippet);
		return "detail";
	}
}
