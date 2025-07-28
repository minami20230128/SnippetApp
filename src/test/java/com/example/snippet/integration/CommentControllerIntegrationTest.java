package com.example.snippet.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.snippet.config.SecurityConfig;
import com.example.snippet.entity.User;
import com.example.snippet.security.CustomUserDetails;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class CommentControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	@Test
	public void test_コメントを投稿() throws Exception {
		var user = new User();
	    user.setId(2);
	    user.setUserName("jiro");
	    var userDetails = new CustomUserDetails(user);
		
		mockMvc.perform(
			post("/snippets/1/comments/new")
			.param("comment", "example2")
			.with(csrf())
			.with(authentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())))
			)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/snippets/1"))
		.andExpect(flash().attribute("message", "コメントを投稿しました。"));
		
		var commentMap = jdbcTemplate.queryForMap("SELECT * FROM comments WHERE created_by = ?", "2");
		assertThat(commentMap.get("comment")).isEqualTo("example2");
	}
	
	@Test
	public void test_投稿されたコメントが空文字() throws Exception {
		var user = new User();
	    user.setId(2);
	    user.setUserName("jiro");
	    var userDetails = new CustomUserDetails(user);
	    
		mockMvc.perform(
			post("/snippets/1/comments/new")
			.param("comment", "")
			.with(csrf())
			.with(authentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())))
			)
		.andExpect(status().isOk())
		.andExpect(view().name("comment"));
	}
}
