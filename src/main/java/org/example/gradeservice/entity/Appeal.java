package org.example.gradeservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.gradeservice.util.AppealStatus;

@Entity
@Table(name = "appeals")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appeal extends BaseEntity {

    @Column(nullable = false)
    Long studentId;

    @Column(nullable = false)
    Long gradeId;

    String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AppealStatus status = AppealStatus.OPEN;
}