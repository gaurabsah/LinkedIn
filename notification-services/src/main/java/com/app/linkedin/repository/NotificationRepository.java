package com.app.linkedin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.linkedin.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
