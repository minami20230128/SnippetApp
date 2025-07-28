package com.example.snippet.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.snippet.entity.Comment;
import com.example.snippet.input.RegisterCommentInput;
import com.example.snippet.repository.CommentRepository;
import com.example.snippet.repository.SnippetRepository;
import com.example.snippet.security.CustomUserDetails;

@Controller
@RequestMapping("/snippets/{snippetId}")
public class CommentController {
	
	private final CommentRepository commentRepository;
	private final SnippetRepository snippetRepository;
	
	public CommentController(CommentRepository commentRepository, SnippetRepository snippetRepository) {
		this.commentRepository = commentRepository;
		this.snippetRepository = snippetRepository;
	}
	
	@GetMapping("/comments/new")
	public String showCommentForm(Model model, @PathVariable int snippetId) {
		var registerCommentInput = new RegisterCommentInput();
		registerCommentInput.setSnippetId(snippetId);
		model.addAttribute("registerCommentInput", registerCommentInput);
		
		return "comment";
	}
	
	@PostMapping("/comments/new")
	public String register(
			@PathVariable int snippetId,
			@Validated RegisterCommentInput registerCommentInput, BindingResult bindingResult, 
			@AuthenticationPrincipal CustomUserDetails userDetails, Model model, RedirectAttributes redirectAttributes
			) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("registerCommentInput", registerCommentInput);
			return "comment";
		}
		
		var comment = new Comment();
		comment.setComment(registerCommentInput.getComment());
		comment.setCreatedAt(LocalDateTime.now());
		comment.setCreatedBy(userDetails.getUser());
		var snippet = this.snippetRepository.findById(snippetId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
		comment.setSnippet(snippet);
		this.commentRepository.save(comment);
		redirectAttributes.addFlashAttribute("message", "コメントを投稿しました。");
		
		return String.format("redirect:/snippets/%d", snippetId);
	}
}
