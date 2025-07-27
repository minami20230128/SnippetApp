package com.example.snippet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.snippet.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	Optional<List<Comment>> findBySnippetId(int snippetId);
}
