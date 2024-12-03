package com.app.linkedin.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.app.linkedin.auth.UserContextHolder;
import com.app.linkedin.entity.Post;
import com.app.linkedin.entity.PostLike;
import com.app.linkedin.event.PostLikedEvent;
import com.app.linkedin.exception.BadRequestException;
import com.app.linkedin.exception.ResourceNotFoundException;
import com.app.linkedin.repository.PostLikeRepository;
import com.app.linkedin.repository.PostsRepository;
import com.app.linkedin.service.PostLikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostsRepository postsRepository;
	private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

	public void likePost(Long postId) {
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("Attempting to like the post with id: {}", postId);

		Post post = postsRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

		boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
		if (alreadyLiked)
			throw new BadRequestException("Cannot like the same post again.");

		PostLike postLike = new PostLike();
		postLike.setPostId(postId);
		postLike.setUserId(userId);
		postLikeRepository.save(postLike);
		log.info("Post with id: {} liked successfully", postId);

		PostLikedEvent postLikedEvent = PostLikedEvent.builder().postId(postId).likedByUserId(userId)
				.creatorId(post.getUserId()).build();

		kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
	}

	public void unlikePost(Long postId) {
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("Attempting to unlike the post with id: {}", postId);
		boolean exists = postsRepository.existsById(postId);
		if (!exists)
			throw new ResourceNotFoundException("Post not found with id: " + postId);

		boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
		if (!alreadyLiked)
			throw new BadRequestException("Cannot unlike the post which is not liked.");

		postLikeRepository.deleteByUserIdAndPostId(userId, postId);

		log.info("Post with id: {} unliked successfully", postId);
	}

}
