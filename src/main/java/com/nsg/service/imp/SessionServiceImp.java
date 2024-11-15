package com.nsg.service.imp;

import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.entity.SessionEntity;
import com.nsg.repository.SessionRepository;
import com.nsg.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    SessionRepository sessionRepository;



    @Override
    public void createSession(SessionCreattionRequest request) {
        SessionEntity session = new SessionEntity();

        session.setDate(request.getDate());

        sessionRepository.save(session);
    }

    @Override
    public Page<SessionResponse> getAllSession(int page, int size) {
        return null;
    }

    @Override
    public SessionResponse getSession(String sessionId) {
        return null;
    }

    @Override
    public SessionResponse updateSession(String sessionId, SessionCreattionRequest request) {
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {

    }
}
