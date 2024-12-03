package com.app.linkedin.service;

import java.util.List;

import com.app.linkedin.dto.PostCreateRequestDto;
import com.app.linkedin.dto.PostDto;

public interface PostService {
	
	PostDto createPost(PostCreateRequestDto postDto);
	
	PostDto getPostById(Long postId);
	
	List<PostDto> getAllPostsOfUser(Long userId);

}
