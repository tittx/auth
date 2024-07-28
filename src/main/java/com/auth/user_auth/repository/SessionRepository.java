package com.auth.user_auth.repository;

import com.auth.user_auth.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findById(long sessionId);
}
