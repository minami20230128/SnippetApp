package com.example.snippet.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.snippet.entity.Snippet;

public interface SnippetRepository extends JpaRepository<Snippet, Integer> {
	public Page<Snippet> findAll(Pageable pageable);
}