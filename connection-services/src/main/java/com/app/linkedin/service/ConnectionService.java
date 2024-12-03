package com.app.linkedin.service;

import java.util.List;

import com.app.linkedin.entity.Person;

public interface ConnectionService {

	List<Person> getFirstDegreeConnections();

	Boolean sendConnectionRequest(Long receiverId);

	Boolean acceptConnectionRequest(Long senderId);

	Boolean rejectConnectionRequest(Long senderId);

}
