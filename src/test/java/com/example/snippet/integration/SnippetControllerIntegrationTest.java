package com.example.snippet.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
public class SnippetControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void test_スニペットの一覧を表示() throws Exception {
		mockMvc.perform(get("/snippets/").with(user("taro").roles("ADMIN"))).andExpect(status().isOk())
				.andExpect(model().attributeExists("snippetsPage")).andExpect(model().attribute("snippets", hasSize(1)))
				.andExpect(model().attribute("snippets", contains(allOf(hasProperty("title", is("example1")),
						hasProperty("code", is("example1")), hasProperty("description", is("example1"))))));
	}

	@Test
	public void test_詳細を表示() throws Exception {
		mockMvc.perform(get("/snippets/1").with(user("taro").roles("ADMIN"))).andExpect(status().isOk())
				.andExpect(model().attribute("snippet",
						allOf(hasProperty("title", is("example1")), hasProperty("code", is("example1")),
								hasProperty("description", is("example1")),

								hasProperty("comments", hasItem(allOf(hasProperty("comment", is("example1"))))))));
	}

	@Test
	public void test_存在しないスニペットの詳細を表示() throws Exception {
		mockMvc.perform(get("/snippets/100").with(user("taro").roles("ADMIN"))).andExpect(status().isNotFound());
	}

	@Test
	public void test_詳細表示時にスニペットIDに半角数字以外を指定() throws Exception {
		mockMvc.perform(get("/snippets/あ").with(user("taro").roles("ADMIN"))).andExpect(status().isBadRequest());
	}

	@Test
	public void test_スニペットを新規登録() throws Exception {
		var user = new User();
		user.setId(1);
		user.setUserName("taro");
		var userDetails = new CustomUserDetails(user);

		mockMvc.perform(post("/snippets/new").param("title", "example2")
				.param("code", "int i = Integer.parseInt(\"2\");").param("description", "example2").with(csrf())
				.with(authentication(
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/snippets/"))
				.andExpect(flash().attribute("message", "登録に成功しました。"));

		var snippetMap = jdbcTemplate.queryForMap("SELECT * FROM snippets WHERE title = ?", "example2");
		assertThat(snippetMap.get("code")).isEqualTo("int i = Integer.parseInt(\"2\");");
		assertThat(snippetMap.get("description")).isEqualTo("example2");
	}

	@Test
	public void test_新規スニペット登録画面での入力内容にバリデーションエラー() throws Exception {
		var user = new User();
		user.setId(1);
		user.setUserName("taro");
		var userDetails = new CustomUserDetails(user);

		mockMvc.perform(post("/snippets/new").param("title", "").param("code", "int i = Integer.parseInt(\"2\");")
				.param("description", "example2").with(csrf())
				.with(authentication(
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))))
				.andExpect(status().isOk()).andExpect(view().name("register"));
	}
}
