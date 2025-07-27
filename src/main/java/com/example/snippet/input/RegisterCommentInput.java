package com.example.snippet.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterCommentInput {
	private Integer snippet_id;
	
	@NotBlank
	@Size(max=128)
	private String comment;

	public Integer getSnippetId() {
		return snippet_id;
	}

	public void setSnippetId(Integer snippet_id) {
		this.snippet_id = snippet_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
