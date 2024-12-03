package com.app.linkedin.consumer;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.app.linkedin.client.ConnectionsClient;
import com.app.linkedin.dto.PersonDto;
import com.app.linkedin.post_service.event.PostCreatedEvent;
import com.app.linkedin.post_service.event.PostLikedEvent;
import com.app.linkedin.service.SendNotification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for(PersonDto connection: connections) {
            sendNotification.send(connection.getUserId(), "Your connection "+postCreatedEvent.getCreatorId()+" has created" +
                    " a post, Check it out");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
        String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }

}
