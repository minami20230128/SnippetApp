package com.example.snippet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.snippet.input.EditSnippetInput;
import com.example.snippet.repository.SnippetRepository;

import java.time.LocalDateTime;  

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
	
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable int id, Model model) {
	    var snippet = this.snippetRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
		model.addAttribute("snippet", snippet);
		return "edit";
	}
	
	@PutMapping("/{id}/edit")
	public String edit(@Validated EditSnippetInput editSnippetInput,
			@PathVariable int id, Model model) {
	    var snippet = this.snippetRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
		snippet.setTitle(editSnippetInput.getTitle());
		snippet.setCode(editSnippetInput.getCode());
		snippet.setUpdatedAt(LocalDateTime.now());
		this.snippetRepository.save(snippet);
		
		return "redirect:/snippets/";
	}
}
