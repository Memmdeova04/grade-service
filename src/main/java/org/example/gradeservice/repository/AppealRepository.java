package org.example.gradeservice.repository;

import org.example.gradeservice.entity.Appeal;
import org.example.gradeservice.util.AppealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppealRepository extends JpaRepository<Appeal, Long> {

    List<Appeal> findByStudentId(Long studentId);
    List<Appeal> findByStatus(AppealStatus status);
    List<Appeal> findByGradeId(Long gradeId);
    void deleteByStudentId(Long studentId);

}