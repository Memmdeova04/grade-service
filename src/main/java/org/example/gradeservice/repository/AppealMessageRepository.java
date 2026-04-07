package org.example.gradeservice.repository;

import org.example.gradeservice.entity.AppealMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppealMessageRepository extends JpaRepository<AppealMessage, Long> {


    List<AppealMessage> findByAppealIdOrderByCreatedAtAsc(Long appealId);
    void deleteByAppealIdIn(List<Long> appealIds);
}