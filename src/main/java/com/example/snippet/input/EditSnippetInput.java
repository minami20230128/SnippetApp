package com.example.snippet.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditSnippetInput {
	private Integer id;

	@NotBlank
	@Size(max=128)
    private String title;

	@NotBlank
    private String code;

	@NotBlank
    private String description;
	
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
