package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.JwtUtil;
import com.example.demo.service.AdminDetailsService;
//
//@RestController
//public class JwtController {
//	
//	@Autowired
//	private AdminDetailsService adminDetailsService;
//	
//	@Autowired
//	private AuthenticationManager authenticationManager;
//	
//	@Autowired
//	private JwtUtil jwtUtil;
//	
//	@PostMapping("/token")
//	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
//		System.out.println(jwtRequest);
//		try {
//			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
//		} catch (UsernameNotFoundException e) {
//			e.printStackTrace();
//			throw new Exception("bad credentials");
//		}
//		
//		UserDetails userDetails = this.adminDetailsService.loadUserByUsername(jwtRequest.getUsername());
//		
//		String token = this.jwtUtil.generateToken(userDetails);
//		System.out.println("JWT "+token);
//		return ResponseEntity.ok(new JwtResponse(token));
//	}
//}
