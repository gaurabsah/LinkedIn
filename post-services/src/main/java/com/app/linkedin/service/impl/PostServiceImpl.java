package com.app.linkedin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.app.linkedin.auth.UserContextHolder;
import com.app.linkedin.dto.PostCreateRequestDto;
import com.app.linkedin.dto.PostDto;
import com.app.linkedin.entity.Post;
import com.app.linkedin.event.PostCreatedEvent;
import com.app.linkedin.exception.ResourceNotFoundException;
import com.app.linkedin.repository.PostsRepository;
import com.app.linkedin.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

	private final PostsRepository postsRepository;
	private final ModelMapper modelMapper;

	private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

	public PostDto createPost(PostCreateRequestDto postDto) {
		Long userId = UserContextHolder.getCurrentUserId();
		Post post = modelMapper.map(postDto, Post.class);
		post.setUserId(userId);

		Post savedPost = postsRepository.save(post);

		PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder().postId(savedPost.getId()).creatorId(userId)
				.content(savedPost.getContent()).build();

		kafkaTemplate.send("post-created-topic", postCreatedEvent);

		return modelMapper.map(savedPost, PostDto.class);
	}

	public PostDto getPostById(Long postId) {
		log.debug("Retrieving post with ID: {}", postId);

		Post post = postsRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
		return modelMapper.map(post, PostDto.class);
	}

	public List<PostDto> getAllPostsOfUser(Long userId) {
		List<Post> posts = postsRepository.findByUserId(userId);

		return posts.stream().map((element) -> modelMapper.map(element, PostDto.class)).collect(Collectors.toList());
	}

}
