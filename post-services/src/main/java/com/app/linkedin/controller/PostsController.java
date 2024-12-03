package com.app.linkedin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.linkedin.dto.PostCreateRequestDto;
import com.app.linkedin.dto.PostDto;
import com.app.linkedin.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

	private final PostService postsService;

	@PostMapping
	public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto) {
		PostDto createdPost = postsService.createPost(postDto);
		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
		PostDto postDto = postsService.getPostById(postId);
		return ResponseEntity.ok(postDto);
	}

	@GetMapping("/users/{userId}/allPosts")
	public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
		List<PostDto> posts = postsService.getAllPostsOfUser(userId);
		return ResponseEntity.ok(posts);
	}

}
