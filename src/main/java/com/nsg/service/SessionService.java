package com.nsg.service;

import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {

    //create session
    void createSession(SessionCreattionRequest request);

    //get all session
    Page<SessionResponse> getAllSession(int page, int size);

    //get a session
    SessionResponse getSession(String sessionId);

    //update a session
    SessionResponse updateSession(String sessionId, SessionCreattionRequest request);

    //delete session
    void deleteSession(String sessionId);

}
