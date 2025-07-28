package com.example.snippet.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.snippet.entity.User;
import com.example.snippet.repository.UserRepository;
import com.example.snippet.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// メールアドレスでユーザーを検索
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		// ロール（権限）を設定
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (user.getIsSupervisor()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // 管理者ロール
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 一般ユーザーロール
		}

		// Spring SecurityのUserオブジェクトを生成して返す
		return new CustomUserDetails(user);
	}
}