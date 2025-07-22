package com.example.snippet.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.snippet.entity.Snippet;
import com.example.snippet.input.EditSnippetInput;
import com.example.snippet.input.RegisterSnippetInput;
import com.example.snippet.repository.SnippetRepository;
import com.example.snippet.security.CustomUserDetails;  

@Controller
@RequestMapping("/snippets")
public class SnippetController {
	private final SnippetRepository snippetRepository;
	
	public SnippetController(SnippetRepository snippetRepository) {
		this.snippetRepository = snippetRepository;
	}
	
	@GetMapping("/")
	public String index(Pageable pageable, Model model) {
		var snippetsPage = this.snippetRepository.findAll(pageable);

        model.addAttribute("snippetsPage", snippetsPage);
		model.addAttribute("snippets", snippetsPage.getContent());
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
	    var editSnippetInput = new EditSnippetInput();
	    editSnippetInput.setId(snippet.getId());
	    editSnippetInput.setTitle(snippet.getTitle());
	    editSnippetInput.setCode(snippet.getCode());
	    editSnippetInput.setDescription(snippet.getDescription());
	    model.addAttribute("editSnippetInput", editSnippetInput);
		return "edit";
	}
	
	@PutMapping("/{id}/edit")
	public String edit(@Validated EditSnippetInput editSnippetInput, BindingResult bindingResult, 
			@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors())
		{
			model.addAttribute("editSnippetInput", editSnippetInput);
			return "edit";
		}
		
	    var snippet = this.snippetRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
		snippet.setTitle(editSnippetInput.getTitle());
		snippet.setCode(editSnippetInput.getCode());
		snippet.setDescription(editSnippetInput.getDescription());
		snippet.setUpdatedAt(LocalDateTime.now());
		this.snippetRepository.save(snippet);
		redirectAttributes.addFlashAttribute("message", "更新に成功しました。");
		
		return "redirect:/snippets/";
	}
	
	@GetMapping("/new")
	public String showRegisterForm(Model model) {
		var registerSnippetInput = new RegisterSnippetInput();
		model.addAttribute("registerSnippetInput", registerSnippetInput);
		return "register";
	}
	
	@PostMapping("/new")
	public String register(@Validated RegisterSnippetInput registerSnippetInput, BindingResult bindingResult, 
			@AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors())
		{
			return "register";
		}
		
		var snippet = new Snippet();
		var user = userDetails.getUser();
		snippet.setCreatedBy(user);
		snippet.setTitle(registerSnippetInput.getTitle());
		snippet.setCode(registerSnippetInput.getCode());
		snippet.setDescription(registerSnippetInput.getDescription());
		snippet.setCreatedAt(LocalDateTime.now());
		this.snippetRepository.save(snippet);
		redirectAttributes.addFlashAttribute("message", "登録に成功しました。");
		
		return "redirect:/snippets/";
	}
}
