package com.app.linkedin.post_service.event;

import lombok.Data;

@Data
public class PostCreatedEvent {
	Long creatorId;
	String content;
	Long postId;
}
