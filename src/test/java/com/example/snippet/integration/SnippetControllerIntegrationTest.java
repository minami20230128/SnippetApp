package com.example.snippet.integration;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.snippet.config.SecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class SnippetControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void test_スニペットの一覧を表示() throws Exception {
		mockMvc.perform(
			get("/snippets/")
			.with(user("taro").roles("ADMIN"))
			)
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("snippetsPage"))
            .andExpect(model().attribute("snippets", hasSize(1)))
            .andExpect(model().attribute("snippets", contains(
        		allOf(
                    hasProperty("title", is("example1")),
                    hasProperty("code", is("example1")),
                    hasProperty("description", is("example1"))
                    )
            )));
	}
	
	@Test
	public void test_詳細を表示() throws Exception {
		mockMvc.perform(
				get("/snippets/1")
				.with(user("taro").roles("ADMIN"))
				)
		.andExpect(status().isOk())
		.andExpect(model().attribute("snippet", allOf(
				hasProperty("title", is("example1")),
                hasProperty("code", is("example1")),
                hasProperty("description", is("example1"))
            )));
	}
	
	@Test
	public void test_存在しないスニペットの詳細を表示() {
		
	}
	
	@Test
	public void test_詳細表示時にスニペットIDに半角数字以外を指定() {
		
	}
	
	@Test
	public void test_スニペットを新規登録() {
		
	}
	
	@Test
	public void test_新規スニペット登録画面での入力内容にバリデーションエラー() {
		
	}
}
