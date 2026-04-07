package org.example.gradeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.gradeservice.util.AppealStatus;

@Entity
@Table(name = "appeals")
@Getter
@Setter
public class Appeal extends BaseEntity {

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long gradeId;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppealStatus status = AppealStatus.OPEN;
}