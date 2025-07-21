package com.example.snippet.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterSnippetInput {
	@NotBlank
	@Size(max=128)
    private String title;

	@NotBlank
    private String code;

	@NotBlank
    private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
