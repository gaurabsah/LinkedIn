package com.app.linkedin.controller;

import com.app.linkedin.entity.Person;
import com.app.linkedin.service.ConnectionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

	private final ConnectionService connectionsService;

	@GetMapping("/first-degree")
	public ResponseEntity<List<Person>> getFirstConnections() {
		return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
	}

	@PostMapping("/request/{userId}")
	public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {
		return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
	}

	@PostMapping("/accept/{userId}")
	public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
		return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
	}

	@PostMapping("/reject/{userId}")
	public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
		return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
	}
}
